package vanille.vocabe.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.repository.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BearerAuthInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("[성공] Bearer 토큰이 필요한 곳에 요청이 들어오면 Interceptor가 실행된다.")
    @Test
    void invokedInterceptor() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        mockMvc.perform(post("/api/v1/words")
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(WordFixture.get(user)))
        ).andDo(print());

        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @DisplayName("[실패] Bearer 토큰이 없으면 NOT_FOUND_TOKEN 예외 발생")
    @Test
    void authInterceptorFailWithNotFoundToken() throws Exception {

        mockMvc.perform(post("/api/v1/words"))
                .andExpect(jsonPath("statusCode").value(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus().toString()));
    }

    @DisplayName("[실패] 토큰 유효하지 않을 때")
    @Test
    void authInterceptorFailWithInvalidToken() throws Exception {
        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "wrongtoken.dwfwdf.wdfxcv")
                )
                .andExpect(jsonPath("statusCode").value(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus().toString()));
    }



}