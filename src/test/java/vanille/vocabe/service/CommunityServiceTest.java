package vanille.vocabe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.*;
import vanille.vocabe.fixture.CommunityFixture;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.repository.*;

import javax.mail.AuthenticationFailedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static vanille.vocabe.payload.CommunityDTO.CommunityDetail;
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

    @DisplayName("커뮤니티 메인 페이지 조회")
    @Test
    void getCommunityMain() {
        Community community = CommunityFixture.getCommunityFixture();
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        CommunityDetail communityDetails = communityService.getCommunityDetails(1L);
        assertEquals(community.getId(), communityDetails.getId());
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

    @DisplayName("커뮤니티 가입 신청")
    @Test
    void requestJoinCommunity() {
        Community community = mock(Community.class);
        User user = mock(User.class);

        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));

        communityService.joinRequest(community.getId(), user);

        then(applicantRepository).should().save(any(Applicant.class));
    }


}