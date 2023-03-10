package vanille.vocabe.service.email;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EmailTokenServiceTest {

    @Mock
    private EmailSender emailSender;
    @Mock
    private EmailTokenRepository emailTokenRepository;

    @InjectMocks
    EmailTokenServiceImpl emailTokenService;

    @DisplayName("[성공] 첫 인증 메일 전송")
    @Test
    void generateFirstEmailToken() {
        String email = "vanille@gmail.com";
        given(emailTokenRepository.findByEmail(email)).willReturn(Optional.ofNullable(null));

        emailTokenService.createEmailToken(email);
        then(emailTokenRepository).should().save(any(EmailToken.class));
    }

    @DisplayName("[성공] 두 번째 인증 메일 전송")
    @Test
    void generateSecondEmailToken() {
        String email = "vanille@gmail.com";
        LocalDateTime now = LocalDateTime.now();
        EmailToken emailToken = EmailToken.createFixtureForTest(email, now);
        UUID tokenId = emailToken.getToken();

        given(emailTokenRepository.findByEmail(email)).willReturn(Optional.of(emailToken));

        emailTokenService.createEmailToken(email);
        Assertions.assertNotEquals(emailToken.getExpirationDate(), now);
        Assertions.assertNotEquals(emailToken.getToken(), tokenId);
    }



}