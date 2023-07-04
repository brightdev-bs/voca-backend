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
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.payload.CommentDTO;
import vanille.vocabe.repository.CommentRepository;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.PostRepository;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.CommentService;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {
    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

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

    @DisplayName("댓글 생성")
    @Test
    void createComment() throws Exception {
        User verifiedUser = UserFixture.getVerifiedUser();
        userRepository.save(verifiedUser);

        Community community = Community.builder()
                .name("test")
                .build();
        communityRepository.save(community);

        Post post = Post.builder()
                .community(community)
                .build();
        postRepository.save(post);

        CommentDTO.CommentForm form = CommentDTO.CommentForm.builder()
                .commentContent("test comment")
                .build();
        mockMvc.perform(post("/api/v1/posts/" + post.getId() + "/comments")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.toString()))
                .andExpect(jsonPath("data").value(Constants.CREATED));
    }

}