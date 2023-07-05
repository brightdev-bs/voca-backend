package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vanille.vocabe.entity.*;
import vanille.vocabe.fixture.CommunityFixture;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.exception.UnverifiedException;
import vanille.vocabe.payload.CommunityDTO;
import vanille.vocabe.repository.*;

import javax.mail.AuthenticationFailedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static vanille.vocabe.payload.CommunityDTO.*;
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
    TopicRepository topicRepository;

    @Mock
    ApplicantRepository applicantRepository;

    @InjectMocks
    CommunityServiceImpl communityService;

    @DisplayName("커뮤니티 메인 페이지 조회")
    @Test
    void getCommunityMain() {
        Community community = CommunityFixture.getCommunityFixture();
        community.getTopics().add(mock(Topic.class));
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        CommunityDetail communityDetails = communityService.getCommunityDetails(1L);
        assertEquals(community.getId(), communityDetails.getId());
        assertEquals(community.getTopics().size(), communityDetails.getTopics().size());
    }

    @DisplayName("커뮤니티 생성하기")
    @Test
    void createCommunity() throws AuthenticationFailedException {
        CommunityUser communityUser = mock(CommunityUser.class);
        Community community = mock(Community.class);
        CommunityForm form = CommunityForm.builder()
                .user(mock(User.class))
                .build();
        given(communityUserRepository.save(any(CommunityUser.class))).willReturn(communityUser);
        given(communityRepository.save(any(Community.class))).willReturn(community);

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

        assertThrows(DuplicatedEntityException.class ,() -> communityService.applyToCommunity(1L, 2L));
    }

    @DisplayName("방장은 회원을 추방할 수 있다.")
    @Test
    void expelCommunityUser() {
        final long MASTER_KEY = 1L;
        Community community = mock(Community.class);
        User user = mock(User.class);
        ExpelleeForm form = mock(ExpelleeForm.class);
        CommunityUser communityUser = mock(CommunityUser.class);
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        given(form.getRequestId()).willReturn(MASTER_KEY);
        given(community.getCreatedBy()).willReturn(MASTER_KEY);
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(communityUserRepository.findCommunityUserByUserAndCommunity(any(Long.class), any(Long.class))).willReturn(Optional.of(communityUser));

        communityService.expelUser(form);
        then(communityUserRepository).should().delete(any(CommunityUser.class));
    }

    @DisplayName("[실패] 방장이 아닌 회원은 추방 권한이 없다")
    @Test
    void expelCommunityUserFail() {
        Community community = mock(Community.class);
        ExpelleeForm form = mock(ExpelleeForm.class);
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        given(form.getRequestId()).willReturn(1L);
        given(community.getCreatedBy()).willReturn(2L);

        assertThrows(InvalidVerificationCodeException.class, () -> communityService.expelUser(form));
    }

    @DisplayName("[실패] 해당 커뮤니티 회원이 아닌 경우")
    @Test
    void expelCommunityFailByNotFound() {
        final long MASTER_KEY = 1L;
        Community community = mock(Community.class);
        User user = mock(User.class);
        ExpelleeForm form = mock(ExpelleeForm.class);
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        given(form.getRequestId()).willReturn(MASTER_KEY);
        given(community.getCreatedBy()).willReturn(MASTER_KEY);
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(communityUserRepository.findCommunityUserByUserAndCommunity(any(Long.class), any(Long.class))).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> communityService.expelUser(form));
    }

    // "커뮤니티 리스트 디폴트 조회 - 10개 조회"
    // -> 통합 테스트로 진행

}