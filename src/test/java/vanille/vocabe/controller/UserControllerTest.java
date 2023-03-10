package vanille.vocabe.controller;

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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("[??????] ???????????? ??????")
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

    @DisplayName("[??????] ????????? SignForm??? ?????? ??? ???")
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
                Arguments.of(" ", "tester", "123456789"), // email ????????? ???????????? ???
                Arguments.of("test@naver.com", " ", "123456789"), // username??? ?????? ???
                Arguments.of("test@naver.com", "tester", " "), // ???????????? ????????? ???????????? ???
                Arguments.of("test2@naver.com", "tester2", "123"), // ??????????????? 8?????? ????????? ???
                Arguments.of("test3@naver.com", "tester3", "1234567")
        );
    }

    @DisplayName("[??????] ?????? ????????? ???????????? URI??? ???????????? ???")
    @Test
    void setVerificationStatusAsVerified() throws Exception {
        EmailToken emailToken = EmailToken.createEmailToken("vanille@gmail.com");
        emailToken.plusExpirationTimeForTest(3L);
        emailTokenRepository.save(emailToken);

        User user = UserFixture.getUnverifiedUser();
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/email?token=" + emailToken.getToken()))
                .andDo(print());

        assertTrue(emailToken.isExpired());
        assertTrue(user.isVerified());

    }

    @DisplayName("[??????] ?????? ?????? ??????????????? ????????? ??? - ???????????? ?????? ???????????? ??????")
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

    @DisplayName("[??????] ?????? ?????? ??????????????? ????????? ??? - ????????? ???????????? ??????")
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

    @DisplayName("[??????] ?????? ????????? token ??????????????? ???????????? ?????? ???")
    @Test
    void confirmVerificationFail() throws Exception {
        mockMvc.perform(get("/api/v1/email?token="))
                .andExpect(jsonPath("statusCode").value(ErrorCode.INVALID_VERIFICATION_CODE.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.INVALID_VERIFICATION_CODE.getMessage()));
    }

    @DisplayName("[??????] ?????????")
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

    @DisplayName("[??????] ????????? - ??????????????? ?????? ??????")
    @Test
    void loginFailWithWrongPassword() throws Exception {
        User user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        UserDTO.LoginForm request = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(ErrorCode.INVALID_PASSWORD.getHttpStatus().toString()))
                .andExpect(jsonPath("data").value(ErrorCode.INVALID_PASSWORD.getMessage()));

    }

    @DisplayName("[??????] ????????? - ???????????? ?????? ??????")
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

}