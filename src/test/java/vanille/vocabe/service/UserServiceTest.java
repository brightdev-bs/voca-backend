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
                .username("vanille")
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
                .password("1kdasdfwcv")
                .username("vanille")
                .build();
        userService.saveUser(userDto);

        then(userRepository).should(never()).save(any(User.class));
        then(emailService).should().sendConfirmEmail(anyString());
    }

    @DisplayName("[실패] 이메일 중복")
    @Test
    void saveUserFailWithDuplicateEmail() {
        User user = UserFixture.getVerifiedUser();
        given(userRepository.findByEmail("vanille@gmail.com")).willReturn(Optional.ofNullable(user));

        UserDTO.SignForm userDto = UserDTO.SignForm.builder()
                .email("vanille@gmail.com")
                .password("1kdasdfwcv")
                .username("vanille")
                .build();
        Assertions.assertThrows(DuplicatedEntityException.class, () -> userService.saveUser(userDto));
    }

}