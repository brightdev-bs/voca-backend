package vanille.vocabe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.EmailTokenFixture;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.config.OpenEntityManagerConfig;
import vanille.vocabe.global.config.SecurityConfig;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.repository.EmailTokenRepository;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.UserService;
import vanille.vocabe.service.email.EmailService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vanille.vocabe.constants.TestConstants.TEST_EMAIL;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("[성공] 회원가입 성공")
    @Test
    void signup() throws Exception {
        UserDTO.SignForm signForm = UserDTO.SignForm.builder()
                .email("test@naver.com")
                .username("tester")
                .password("1a2s3d4f5g")
                .build();

        mockMvc.perform(post("/api/v1/sign-up")
                        .content(objectMapper.writeValueAsString(signForm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("statusCode").value("200 OK"))
                .andExpect(jsonPath("data").value("test@naver.com"));
    }

    @DisplayName("[실패] 잘못된 SignForm이 넘어 올 때")
    @ParameterizedTest
    @MethodSource("signupForm")
    void signupWithWrongRequest(String email, String username, String password) throws Exception {
        UserDTO.SignForm signForm = UserDTO.SignForm.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/sign-up")
                        .content(objectMapper.writeValueAsString(signForm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

    }

    private static Stream<Arguments> signupForm() {
        return Stream.of(
                Arguments.of(" ", "tester", "123456789"), // email 입력이 안되었을 때
                Arguments.of("test@naver.com", " ", "123456789"), // username이 없을 때
                Arguments.of("test@naver.com", "tester", " "), // 비밀번호 입력이 안되었을 때
                Arguments.of("test2@naver.com", "tester2", "123"), // 비밀번호가 8자리 미만일 때
                Arguments.of("test3@naver.com", "tester3", "1234567")
        );
    }

    @DisplayName("[성공] 확인 메일을 클릭해서 URI로 접속했을 때")
    @Test
    void setVerificationStatusAsVerified() throws Exception {
        EmailToken emailToken = EmailToken.createEmailToken("vanille@gmail.com");
        emailTokenRepository.save(emailToken);

        User user = UserFixture.getUnverifiedUser();
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/email?token=" + emailToken.getToken()))
                .andDo(print());

        assertTrue(emailToken.isExpired());
        assertTrue(user.isVerified());
    }

    @DisplayName("[실패] 확인 메일 유효시간이 지났을 때 - 회원가입하지 않은 이메일인 경우")
    @Test
    void expiredVerificationEmailFailWithInvalidEmail() throws Exception {
        EmailToken emailToken = EmailToken.createEmailToken("test@naver.com");
        emailToken.plusExpirationTimeForTest(-3L);
        emailTokenRepository.save(emailToken);

        mockMvc.perform(get("/api/v1/email?token=" + emailToken.getToken()))
                .andExpect(jsonPath("statusCode").value(ErrorCode.NOT_FOUND_USER.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.NOT_FOUND_USER.getMessage()))
                .andDo(print());
    }

    @DisplayName("[실패] 확인 메일 유효시간이 지났을 때 - 회원가입한 이메일인 경우")
    @Test
    void expiredVerificationEmail() throws Exception {
        User user = UserFixture.getUnverifiedUser();
        userRepository.saveAndFlush(user);
        EmailToken emailToken = EmailToken.createEmailToken("vanille@gmail.com");
        emailToken.plusExpirationTimeForTest(-3L);
        emailTokenRepository.save(emailToken);

        mockMvc.perform(get("/api/v1/email?token=" + emailToken.getToken()))
                .andExpect(jsonPath("statusCode").value(ErrorCode.EXPIRED_TOKEN.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.EXPIRED_TOKEN.getMessage()))
                .andDo(print());
    }

    @DisplayName("[실패] 확인 메일로 token 파라미터가 넘어오지 않을 때")
    @Test
    void confirmVerificationFail() throws Exception {
        mockMvc.perform(get("/api/v1/email?token="))
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(ErrorCode.NOT_FOUND_EMAIL_TOKEN.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.NOT_FOUND_EMAIL_TOKEN.getMessage()));
    }

    @DisplayName("[성공] 로그인")
    @Test
    void login() throws Exception {
        User user = UserFixture.getVerifiedUser();
        user.setPasswordForTest(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        UserDTO.LoginForm request = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("{bcrypt}1kdasdfwcv")
                .build();

        mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("statusCode").value("200 OK"));
    }

    @DisplayName("[실패] 로그인 - 비밀번호가 틀린 경우")
    @Test
    void loginFailWithWrongPassword() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        UserDTO.LoginForm request = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("{bcrypt}wrong password")
                .build();

        mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(ErrorCode.INVALID_PASSWORD.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.INVALID_PASSWORD.getMessage()));

    }

    @DisplayName("[실패] 로그인 - 인증되지 않은 유저")
    @Test
    void loginFailWithUnverified() throws Exception {
        User user = UserFixture.getUnverifiedUser();
        userRepository.save(user);

        UserDTO.LoginForm request = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("{bcrypt}1kdasdfwcv")
                .build();

        mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(ErrorCode.UNVERIFIED_USER.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.UNVERIFIED_USER.getMessage()));
    }

    @DisplayName("[성공] 비밀번호 찾기")
    @Test
    void findPassword() throws Exception {
        String email = UserFixture.email;
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/password?email=" + email))
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("data").value(email));
    }

    @DisplayName("[성공] 비밀번호 변경")
    @Test
    void changePassword() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        EmailToken emailToken = EmailTokenFixture.createEmailToken();
        emailTokenRepository.save(emailToken);

        String password = "mango!1234";
        UserDTO.PasswordForm request = UserDTO.PasswordForm.builder()
                .password(password)
                .password2(password)
                .token(emailToken.getToken().toString())
                .build();

        mockMvc.perform(post("/api/v1/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("data").value(true));
    }

    @DisplayName("[실패] 비밀번호 변경 - 비밀번호 서로 다름")
    @Test
    void changePasswordFailByDifferentPassword() throws Exception {
        EmailToken emailToken = EmailTokenFixture.createEmailToken();
        emailTokenRepository.save(emailToken);

        UserDTO.PasswordForm request = UserDTO.PasswordForm.builder()
                .password("password1234")
                .password2("wrongpassword")
                .token(emailToken.getToken().toString())
                .build();

        mockMvc.perform(post("/api/v1/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("data").value(ErrorCode.INVALID_PASSWORD.getMessage()));
    }

    @DisplayName("[실패] 비밀번호 변경 - 토큰 만료")
    @Test
    void changePasswordFailByExpiredToken() throws Exception {
        EmailToken emailToken = EmailTokenFixture.createExpiredDateEmailToken();
        emailTokenRepository.save(emailToken);

        String password = "samePassword123";
        UserDTO.PasswordForm request = UserDTO.PasswordForm.builder()
                .password(password)
                .password2(password)
                .token(emailToken.getToken().toString())
                .build();

        mockMvc.perform(post("/api/v1/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("data").value(ErrorCode.EXPIRED_TOKEN.getMessage()));
    }

}