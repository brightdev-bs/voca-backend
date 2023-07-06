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
import vanille.vocabe.entity.*;
import vanille.vocabe.fixture.CommunityFixture;
import vanille.vocabe.fixture.TopicFixture;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.repository.*;
import vanille.vocabe.service.CommunityService;

import javax.transaction.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ApplicantRepository applicantRepository;

    @Autowired
    private TopicRepository topicRepository;

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

        mockMvc.perform(post("/api/v1/community/form")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.toString()));
    }

    @DisplayName("커뮤니티 메인 페이지 조회")
    @Test
    void getCommunityDetails() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        Community community = CommunityFixture.getCommunityFixture();
        communityRepository.save(community);

        Topic topic = TopicFixture.getTopicFixture(community);
        Topic topic2 = TopicFixture.getTopicFixture(community);
        topicRepository.save(topic);
        topicRepository.save(topic2);

        mockMvc.perform(get("/api/v1/community/" + community.getId()))
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()));
    }

    @DisplayName("커뮤니티 디폴트 조회 - 10개 ")
    @Test
    void getCommunities() {
        setData();
        List<HomeResponse> communities = communityService.getCommunities();
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

    @DisplayName("커뮤니티 가입 신청")
    @Test
    void requestJoinCommunity() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        JoinForm form = JoinForm.builder()
                .content("teest content")
                .build();
        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/members")
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("data").value(Constants.SUCCESS));
    }

    @DisplayName("[실패] 커뮤니티 가입 신청 - 승인 대기중 다중 요청한 경우")
    @Test
    void joinCommunityFailByMultipleRequest() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        Applicant applicant = Applicant.builder()
                .user(user)
                .community(community)
                .build();
        applicantRepository.save(applicant);


        JoinForm form = JoinForm.builder()
                .content("teest content")
                .build();
        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/members")
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("data").value(ErrorCode.DUPLICATED_REQUEST.getMessage()));
    }

    @DisplayName("[실패] 커뮤니티 가입 신청 - 이전에 거절당한 경우")
    @Test
    void joinCommunityFailByRejected() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        Applicant applicant = Applicant.builder()
                .user(user)
                .community(community)
                .build();
        applicant.setChecked(true);
        applicantRepository.save(applicant);


        JoinForm form = JoinForm.builder()
                .content("teest content")
                .build();
        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/members")
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("data").value(ErrorCode.REJECTED_REQUEST.getMessage()));
    }


}