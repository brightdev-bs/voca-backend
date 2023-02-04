package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailSender emailSender;
    private final EmailTokenRepository emailTokenRepository;

    @Override
    public Long createEmailToken(String email) {
        Assert.notNull(email, "받는 이메일은 필수입니다.");

        EmailToken emailToken = EmailToken.createEmailToken(email);
        emailTokenRepository.save(emailToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("회원가입 이메일 인증 메일");
        mailMessage.setText("http://localhost:8080/email?token=" + emailToken.getId());
        emailSender.sendEmail(mailMessage);

        return emailToken.getId();
    }

    @Override
    public EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId) {
        Optional<EmailToken> emailToken = emailTokenRepository
                .findByIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false);

        // 토큰이 없다면 예외 발생
        return emailToken.orElseThrow(() -> new RuntimeException());
    }

}
