package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserRole;
import vanille.vocabe.fixture.EmailTokenFixture;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidPasswordException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.exception.UnverifiedException;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.repository.UserCacheRepository;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.email.EmailServiceImpl;
import vanille.vocabe.service.email.EmailTokenServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static vanille.vocabe.constants.TestConstants.TEST_EMAIL;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private EmailTokenServiceImpl emailTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCacheRepository userCacheRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("[성공] 1. 첫 가입 시도")
    @Test
    void saveUser() {
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(null));

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .username("test")
                .build();
        userService.saveUser(userDto);

        then(userRepository).should().save(any(User.class));
        then(passwordEncoder).should().encode(any(String.class));
        then(emailService).should().sendSignUpConfirmEmail(anyString());
    }

    @DisplayName("[성공] 2. 가입 시도 여러번했지만, 인증은 안 한 경우")
    @Test
    void saveUserMoreThanOne() {
        User user = UserFixture.getUnverifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.encode(any(String.class))).willReturn("changedPassword");

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("vanille@gmail.com")
                .password("changedPassword")
                .username("changeName")
                .build();



        userService.saveUser(userDto);
        assertEquals("changeName", user.getUsername());
        assertEquals("changedPassword", user.getPassword());
    }

    @DisplayName("[실패] 회원가입 - 이메일 중복")
    @Test
    void saveUserFailWithDuplicateEmail() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .username("test")
                .build();
        Assertions.assertThrows(DuplicatedEntityException.class, () -> userService.saveUser(userDto));
    }

    @DisplayName("[실패] 회원가입 - 이름 중복")
    @Test
    void saveUserFailWithDuplicateName() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.ofNullable(null));
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.ofNullable(user));

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .build();
        Assertions.assertThrows(DuplicatedEntityException.class, () -> userService.saveUser(userDto));
    }

    @DisplayName("[성공] 로그인")
    @Test
    void loginUser() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("{bcrypt}1kdasdfwcv", "{bcrypt}1kdasdfwcv")).willReturn(true);
        UserDTO.LoginForm loginForm = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("{bcrypt}1kdasdfwcv")
                .build();

        User loginUser = userService.login(loginForm);
        assertEquals(user.getEmail(), loginUser.getEmail());
        assertEquals(user.getPassword(), loginUser.getPassword());
        assertEquals(user.getUsername(), loginUser.getUsername());
    }

    @DisplayName("[실패] 로그인 - 존재하지 않는 이메일")
    @Test
    void loginUserFailWrongEmail() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(null));

        UserDTO.LoginForm loginForm = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("wdfwdfxcv")
                .build();
        Assertions.assertThrows(NotFoundException.class, () -> userService.login(loginForm));

    }

    @DisplayName("[실패] 로그인 - 인증하지 않은 사용자")
    @Test
    void loginUserFailUnverified() {
        User user = UserFixture.getUnverifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.of(user));

        UserDTO.LoginForm loginForm = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .build();
        Assertions.assertThrows(UnverifiedException.class, () -> userService.login(loginForm));
    }

    @DisplayName("[실패] 로그인 - 패스워드 오류")
    @Test
    void loginUserFailWrongPassword() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.of(user));

        UserDTO.LoginForm loginForm = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("wdfwdfxcv")
                .build();
        Assertions.assertThrows(InvalidPasswordException.class, () -> userService.login(loginForm));
    }

    @DisplayName("[실패] 로그인 - 소셜로그인 회원")
    @Test
    void loginUserFailWithDifferentPlatform() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(User.ofSocial("test", "test@gmail.com")));
        UserDTO.LoginForm loginForm = UserDTO.LoginForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .build();
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userService.login(loginForm));
    }


    @DisplayName("[성공] 이메일 - 사용자 찾기")
    @Test
    void findUserByEmail() {
        given(userRepository.findByEmail(UserFixture.email)).willReturn(Optional.of(UserFixture.getVerifiedUser()));
        User user = userService.findUserByEmail(UserFixture.email);
        assertEquals(UserFixture.email, user.getEmail());
    }

    @DisplayName("[성공] 패스워드 변경")
    @Test
    void changePassword() {
        String newPassword = "mango1234";
        UserDTO.PasswordForm form = UserDTO.PasswordForm.builder()
                .password(newPassword)
                .password2(newPassword)
                .token("temporary value")
                .build();

        EmailToken emailToken = EmailTokenFixture.createEmailToken();
        User user = UserFixture.getVerifiedUser(TEST_EMAIL);
        given(emailTokenService.findByToken(any(String.class))).willReturn(Optional.of(emailToken));
        given(emailTokenService.validateToken(emailToken)).willReturn(true);
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        assertTrue(userService.changeUserPassword(form));
    }

    @DisplayName("[실패] 패스워드 변경 - 패스워드 불일치")
    @Test
    void changePasswordFailByDifferentPassword() {
        UserDTO.PasswordForm form = UserDTO.PasswordForm.builder()
                .password("password1")
                .password2("notSamePassword")
                .token("temporary value")
                .build();

        EmailToken emailToken = EmailTokenFixture.createEmailToken();
        given(emailTokenService.findByToken(any(String.class))).willReturn(Optional.of(emailToken));
        assertFalse(userService.changeUserPassword(form));
    }

    @DisplayName("[실패] 패스워드 변경 - 토큰 만료")
    @Test
    void changePasswordFailByExpiredToken() {
        UserDTO.PasswordForm form = UserDTO.PasswordForm.builder()
                .password("password1")
                .password2("password1")
                .token("expired date token")
                .build();

        EmailToken emailToken = EmailTokenFixture.createExpiredDateEmailToken();
        given(emailTokenService.findByToken(any(String.class))).willReturn(Optional.of(emailToken));
        assertFalse(userService.changeUserPassword(form));

        emailToken = EmailTokenFixture.createEmailToken();
        given(emailTokenService.findByToken(any(String.class))).willReturn(Optional.of(emailToken));
        assertFalse(userService.changeUserPassword(form));
    }

    @DisplayName("[성공] 구글 회원가입 - 소셜 로그인 회원은 이메일 인증 없이 인증된 채로 가입된다.")
    @Test
    void googleSignup() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(User.ofSocial(googleUser.getName(), googleUser.getEmail()));

        User user = userService.googleSignup(googleUser);
        then(userRepository).should().save(any(User.class));
        assertTrue(user.isVerified());
        assertNull(user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
    }

    @DisplayName("[실패] 구글 회원가입 - 일반 회원가입으로 이전에 이메일로 가입한 경우. ")
    @Test
    void googleSignupFailWithoutConnection() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(mock(User.class)));

        assertThrows(DuplicatedEntityException.class, () -> userService.googleSignup(googleUser));
    }

    @DisplayName("[실패] 구글 회원가입 - 같은 유저이름이 존재하는 경우")
    @Test
    void googleSignupFailWithSameUsername() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(mock(User.class)));

        assertThrows(DuplicatedEntityException.class, () -> userService.googleSignup(googleUser));
    }

    @DisplayName("[성공] 구글 로그인")
    @Test
    void googleLogin() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(User.ofSocial(googleUser.getName(), googleUser.getEmail())));
        User user = userService.googleLogin(googleUser);
        assertEquals("test", user.getUsername());
        assertEquals("test@gmail.com", user.getEmail());
    }

    @DisplayName("[실패] 구글 로그인 - 일반 회원가입 유저")
    @Test
    void googleLoginFailWithNormalSignup() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(User.of(googleUser.getName(), googleUser.getEmail(), "temporaryPassword1")));
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userService.googleLogin(googleUser));
    }

    @DisplayName("[실패] 구글 로그인 - 회원가입하지 않은 경우")
    @Test
    void googleLoginFailWithNotFound() {
        UserDTO.GoogleUser googleUser = UserFixture.getGoogleUser();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.googleLogin(googleUser));
    }

}