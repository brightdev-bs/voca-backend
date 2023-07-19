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
    public void joinRequest(Long communityId, User user) {
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        Applicant applicant = Applicant.builder()
                .user(user)
                .community(community)
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
                .build();
        applicantRepository.save(applicant);
    }

    @Override
    public List<HomeResponse> getCommunities() {
        PageRequest pageable = PageRequest.of(0, Constants.DEFAULT_SIZE);
        Page<Community> communities = communityRepository.findAll(pageable);
        return communities.stream().map(HomeResponse::from).collect(Collectors.toList());
    }

}
