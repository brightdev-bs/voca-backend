package vanille.vocabe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.CommunityUserRepository;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.CommunityService;

import javax.transaction.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;
import static vanille.vocabe.payload.CommunityDTO.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class CommunityControllerTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityUserRepository communityUserRepository;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("커뮤니티 생성")
    @Test
    void createCommunity() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        CommunityForm form = CommunityForm.builder()
                .name("test community")
                .description("test")
                .isPublic(true)
                .total(10)
                .build();

        mockMvc.perform(post("/api/v1/community")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.toString()));
    }

    @DisplayName("커뮤니티 디폴트 조회 - 10개 ")
    @Test
    void getCommunities() {
        setData();
        List<Response> communities = communityService.getCommunities();
        Assertions.assertEquals(10, communities.size());
    }

    private void setData() {
        Community community = Community.builder().name("sdfsdfsdf").build();
        Community community2 = Community.builder().name("sdfsdfsdf").build();
        Community community3 = Community.builder().name("sdfsdfsdf").build();
        Community community4 = Community.builder().name("sdfsdfsdf").build();
        Community community5 = Community.builder().name("sdfsdfsdf").build();
        Community community6 = Community.builder().name("sdfsdfsdf").build();
        Community community7 = Community.builder().name("sdfsdfsdf").build();
        Community community8 = Community.builder().name("sdfsdfsdf").build();
        Community community9 = Community.builder().name("sdfsdfsdf").build();
        Community community10 = Community.builder().name("sdfsdfsdf").build();
        Community community11 = Community.builder().name("sdfsdfsdf").build();

        communityRepository.save(community);
        communityRepository.save(community2);
        communityRepository.save(community3);
        communityRepository.save(community4);
        communityRepository.save(community5);
        communityRepository.save(community6);
        communityRepository.save(community7);
        communityRepository.save(community8);
        communityRepository.save(community9);
        communityRepository.save(community10);
        communityRepository.save(community11);
    }
}