package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Applicant;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.CommunityUser;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.ApplicantRepository;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.CommunityUserRepository;
import vanille.vocabe.repository.UserRepository;

import javax.security.sasl.AuthenticationException;
import java.util.List;

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
    public Community saveCommunity(CommunityForm form) {

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
}
