package vanille.vocabe.service.email;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME = 5L;

    @Mock
    private EmailTokenService emailTokenService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailServiceImpl emailService;


    @DisplayName("[성공] 이메일 인증")
    @Test
    void verifyEmail() {
        String email = "vanille@gmail.com";
        EmailToken emailToken = EmailToken.createFixtureForTest(email, LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME));
        given(emailTokenService.findByIdAndExpirationDateAfterAndExpired(anyString())).willReturn(Optional.of(emailToken));

        User user = UserFixture.getUnverifiedUser();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        emailService.verifyEmail(emailToken.getId().toString());
        Assertions.assertTrue(emailToken.isExpired());
        Assertions.assertTrue(user.isVerified());
    }

}