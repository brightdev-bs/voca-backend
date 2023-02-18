package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidPasswordException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.exception.UnverifiedException;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.email.EmailServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailServiceImpl emailService;
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
        then(emailService).should().sendConfirmEmail(anyString());
    }

    @DisplayName("[성공] 2. 가입 시도 여러번했지만, 인증은 안 한 경우")
    @Test
    void saveUserMoreThanOne() {
        User user = UserFixture.getUnverifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("vanille@gmail.com")
                .password("changedPassword")
                .username("changeName")
                .build();
        userService.saveUser(userDto);

        then(emailService).should().sendConfirmEmail(anyString());
        Assertions.assertEquals("changedPassword", user.getPassword());
        Assertions.assertEquals("changeName", user.getUsername());
    }

    @DisplayName("[실패] 이메일 중복")
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

    @DisplayName("[성공] 로그인")
    @Test
    void loginUser() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));

        UserDTO.loginForm loginForm = UserDTO.loginForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .build();

        User loginUser = userService.login(loginForm);
        Assertions.assertEquals(user.getEmail(), loginUser.getEmail());
        Assertions.assertEquals(user.getPassword(), loginUser.getPassword());
        Assertions.assertEquals(user.getUsername(), loginUser.getUsername());
    }

    @DisplayName("[실패] 로그인 - 존재하지 않는 이메일")
    @Test
    void loginUserFailWrongEmail() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(null));

        UserDTO.loginForm loginForm = UserDTO.loginForm.builder()
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

        UserDTO.loginForm loginForm = UserDTO.loginForm.builder()
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

        UserDTO.loginForm loginForm = UserDTO.loginForm.builder()
                .email("vanille@gmail.com")
                .password("wdfwdfxcv")
                .build();
        Assertions.assertThrows(InvalidPasswordException.class, () -> userService.login(loginForm));
    }


}