package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Applicant;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.CommunityUser;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.repository.ApplicantRepository;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.CommunityUserRepository;
import vanille.vocabe.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static vanille.vocabe.payload.CommunityDTO.CommunityForm;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CommunityRepository communityRepository;

    @Mock
    CommunityUserRepository communityUserRepository;

    @Mock
    ApplicantRepository applicantRepository;

    @InjectMocks
    CommunityServiceImpl communityService;

    @DisplayName("커뮤니티 생성하기")
    @Test
    void createCommunity() {
        CommunityUser communityUser = mock(CommunityUser.class);
        Community community = mock(Community.class);
        given(communityUserRepository.save(any(CommunityUser.class))).willReturn(communityUser);
        given(communityRepository.save(any(Community.class))).willReturn(community);

        CommunityForm form = CommunityForm.builder().build();
        communityService.saveCommunity(form);

        then(communityRepository).should().save(any(Community.class));
        then(communityUserRepository).should().save(any(CommunityUser.class));
    }

    @DisplayName("커뮤니티 가입 신청")
    @Test
    void applyToCommunity() {
        User user = mock(User.class);
        Community community = mock(Community.class);
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));

        communityService.applyToCommunity(1L, 2L);

        then(applicantRepository).should().save(any(Applicant.class));
    }

    @DisplayName("[실패] 커뮤니티 가입 신청 - 이미 가입한 회원")
    @Test
    void applyToCommunityFail() {
        User user = mock(User.class);
        Community community = mock(Community.class);
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));

        CommunityUser cu = CommunityUser.builder().user(user).community(community).build();
        List<CommunityUser> cuList = List.of(cu);
        given(cu.getUser().getId()).willReturn(1L);
        given(cu.getCommunity().getId()).willReturn(2L);
        given(community.getCommunityUsers()).willReturn(cuList);

        Assertions.assertThrows(DuplicatedEntityException.class ,() -> communityService.applyToCommunity(1L, 2L));
    }

}