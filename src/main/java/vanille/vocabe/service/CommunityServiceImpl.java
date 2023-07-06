package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.*;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.*;

import javax.mail.AuthenticationFailedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static vanille.vocabe.payload.ApplicantDTO.*;
import static vanille.vocabe.payload.CommunityDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityServiceImpl implements CommunityService {

    private final UserRepository userRepository;

    private final CommunityRepository communityRepository;

    private final CommunityUserRepository communityUserRepository;
    private final ApplicantRepository applicantRepository;

    @Override
    public CommunityDetail getCommunityDetails(Long communityId) {
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        return CommunityDetail.from(community);
    }

    @Transactional
    @Override
    public Community saveCommunity(CommunityForm form) throws AuthenticationFailedException {
        if(form.getUser() == null) throw new AuthenticationFailedException(ErrorCode.NO_AUTHORITY.toString());
        Community community = communityRepository.save(form.toEntity());
        CommunityUser communityUser = CommunityUser.builder()
                .user(form.getUser())
                .community(community)
                .build();
        communityUserRepository.save(communityUser);
        return community;
    }

    @Transactional
    @Override
    public void joinRequest(JoinForm form) {
        Community community = communityRepository.findById(form.getCommunityId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        User user = form.getUser();

        Optional<Applicant> byApplicant = applicantRepository.findApplicantByUserAndCommunity(user, community);
        if(byApplicant.isPresent()) {
            Applicant applicant = byApplicant.get();
            if(applicant.isChecked() && !applicant.isAccepted())
                throw new DuplicatedEntityException(ErrorCode.REJECTED_REQUEST);
            if(!applicant.isChecked()) {
                throw new DuplicatedEntityException(ErrorCode.DUPLICATED_REQUEST);
            }
            throw new DuplicatedEntityException(ErrorCode.DUPLICATED_USER);
        }

        Applicant applicant = Applicant.builder()
                .user(form.getUser())
                .community(community)
                .motive(form.getContent())
                .build();
        applicantRepository.save(applicant);
    }

    @Transactional
    @Override
    public void applyToCommunity(Long userId, Long communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));

        List<CommunityUser> communityUsers = community.getCommunityUsers();
        boolean exist = communityUsers.stream().anyMatch(cu ->
                cu.getUser().getId().equals(user.getId()) &&
                cu.getCommunity().getId().equals(communityId)
        );
        if(exist)
            throw new DuplicatedEntityException(ErrorCode.DUPLICATED_USER);

        Applicant applicant = Applicant.builder()
                .user(user)
                .community(community)
                .accepted(false)
                .build();
        applicantRepository.save(applicant);
    }

    @Transactional
    @Override
    public void expelUser(ExpelleeForm form) {
        Community community = communityRepository.findById(form.getCommunityId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));

        if(!isMaster(form, community))
            throw new InvalidVerificationCodeException(ErrorCode.NO_AUTHORITY);

        User expelle = userRepository.findById(form.getExpelleeId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        CommunityUser communityUser = communityUserRepository.findCommunityUserByUserAndCommunity(community.getId(), expelle.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        communityUserRepository.delete(communityUser);
    }

    private boolean isMaster(ExpelleeForm form, Community community) {
        return form.getRequestId().equals(community.getCreatedBy());
    }

    @Override
    public List<HomeResponse> getCommunities() {
        PageRequest pageable = PageRequest.of(0, Constants.DEFAULT_SIZE);
        Page<Community> communities = communityRepository.findAll(pageable);
        return communities.stream().map(HomeResponse::from).collect(Collectors.toList());
    }

    @Override
    public List<Community> getCommunities(String name) {
        return communityRepository.findCommunitiesByNameContaining(name);
    }

    @Override
    public List<ApplicantResponse> getApplicants(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        List<Applicant> applicants = applicantRepository.findApplicantByCommunityAndCheckedFalse(community);
        return applicants.stream().map(ApplicantResponse::from).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void responseForApplicant(ApplicantDetail form) {
        Applicant applicant = applicantRepository.findById(form.getApplicantId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICANT));
        Community community = communityRepository.findById(form.getCommunityId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        User user = userRepository.findById(form.getUser().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        if(community.getCreatedBy().equals(user.getId())){
            if(form.isAccept()) {
                applicant.acceptMember();

                CommunityUser communityUser = CommunityUser.builder()
                        .user(applicant.getUser())
                        .community(community)
                        .build();
                communityUserRepository.save(communityUser);
            }
            else
                applicant.rejectMember();
        } else {
            throw new IllegalStateException(ErrorCode.NO_AUTHORITY.toString());
        }
    }
}
