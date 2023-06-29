package vanille.vocabe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Community;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.payload.CommunityDTO;
import vanille.vocabe.repository.CommunityRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static vanille.vocabe.payload.CommunityDTO.*;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    CommunityRepository communityRepository;

    @InjectMocks
    CommunityServiceImpl communityService;

    @DisplayName("커뮤니티 생성하기")
    @Test
    void createCommunity() {
        CommunityForm form = CommunityForm.builder()
                .name("커뮤니티 생성 테스트")
                .description("커뮤니티 생성 되는지 확인하기")
                .open(false)
                .totalNumber(50)
                .build();

        communityService.saveCommunity(form);
        then(communityRepository).should().save(any(Community.class));
    }

}