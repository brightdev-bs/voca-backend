package vanille.vocabe.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.repository.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;
import static vanille.vocabe.global.Constants.VERIFIED_USER_EMAIL;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BearerAuthInterceptorTest {

    private String key = "voca-backend-secretkey-application-yml-local";

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
        User user = getVerifiedUser();

        mockMvc.perform(get("/api/v1/my-page")
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

    @DisplayName("[실패] 토큰이 만료되었을 때")
    @Test
    void authInterceptorFailWithExpiredToken() throws Exception {
        User user = UserFixture.getVerifiedUser("kim", "expiredTokenTest@naver.com");
        userRepository.save(user);

        final String token;
        try {
            token = JwtTokenUtils.generateAccessToken("kim", 1000L, key);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WordFixture.get(user)))
                )
                .andExpect(jsonPath("statusCode").value(ErrorCode.EXPIRED_TOKEN.getHttpStatus().toString()))
                .andDo(print());
    }

    @DisplayName("[실패] 토큰 유효하지 않을 때")
    @Test
    void authInterceptorFailWithInvalidToken() throws Exception {
        User user = getVerifiedUser();
        userRepository.save(user);
        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "wrongtoken.dwfwdf.wdfxcv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WordFixture.get(user)))
                )
                .andExpect(jsonPath("statusCode").value(ErrorCode.INVALID_TOKEN.getHttpStatus().toString()))
                .andDo(print());
    }

    private User getVerifiedUser() {
        return userRepository.findByEmail(VERIFIED_USER_EMAIL).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

}