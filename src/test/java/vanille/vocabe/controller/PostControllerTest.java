package vanille.vocabe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.*;
import vanille.vocabe.fixture.TopicFixture;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.payload.PostDTO;
import vanille.vocabe.payload.TopicDTO;
import vanille.vocabe.repository.*;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;
import static vanille.vocabe.payload.PostDTO.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityUserRepository communityUserRepository;

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

    @DisplayName("포스트 리스트 조회")
    @Test
    void getPosts() throws Exception {
        Community community = Community.builder()
                .name("test community")
                .build();
        communityRepository.save(community);

        Topic topic = TopicFixture.getTopicFixture(community);
        topicRepository.save(topic);

        Post post = createPost(community, topic);
        Post post2 = createPost(community, topic);
        Post post3 = createPost(community, topic);
        postRepository.save(post);
        postRepository.save(post2);
        postRepository.save(post3);

        mockMvc.perform(get("/api/v1/community/" + community.getId() + "/topics/" + topic.getId()))
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("data").isArray());
    }

    @DisplayName("포스트 생성")
    @Test
    void createPost() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);
        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        CommunityUser communityUser = CommunityUser.builder()
                .user(user)
                .community(community)
                .build();
        communityUserRepository.save(communityUser);
        community.getCommunityUsers().add(communityUser);
        user.getCommunities().add(communityUser);

        PostForm form = PostForm.builder()
                .communityId(community.getId())
                .postContent("testsfsdfsfsdfsdsdfds")
                .build();

        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/posts")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.toString()));
    }

    @DisplayName("[실패] 포스트 생성 - 커뮤니티 회원이 아니면 글쓰기 불가")
    @Test
    void createPostFailByAuthentication() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);
        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        PostForm form = PostForm.builder()
                .communityId(community.getId())
                .postContent("testsfsdfsfsdfsdsdfds")
                .build();

        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/posts")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("data").value(ErrorCode.NO_AUTHORITY.name()));
    }



    private Post createPost(Community community, Topic topic) {
        Post post = Post.builder()
                .community(community)
                .content("테스트 포스트")
                .topic(topic)
                .build();
        post.setCreatedByForTest(1L);
        topic.getPosts().add(post);
        return post;
    }

}