package vanille.vocabe.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.CommunityUser;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.PostFixture;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.CommunityUserRepository;
import vanille.vocabe.repository.PostRepository;
import vanille.vocabe.repository.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;
import static vanille.vocabe.payload.PostDTO.PostForm;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityUserRepository communityUserRepository;
    @Autowired
    private PostRepository postRepository;

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

        mockMvc.perform(post("/api/v1/community/" + community.getId() + "/posts/form")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.toString()));
    }

    @DisplayName("포스트 리스트 조회")
    @Test
    void getPosts() throws Exception {
        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        Post post = postRepository.save(PostFixture.getPostFixture(community));
        Post post2 = postRepository.save(PostFixture.getPostFixture(community));
        Post post3 = postRepository.save(PostFixture.getPostFixture(community));
        Post post4 = postRepository.save(PostFixture.getPostFixture(community));
        community.addPost(post);
        community.addPost(post2);
        community.addPost(post3);
        community.addPost(post4);

        mockMvc.perform(get("/api/v1/community/" + community.getId() + "/posts"))
                .andDo(print())
                .andExpect(jsonPath("$.data.length()").value(4));
    }

}