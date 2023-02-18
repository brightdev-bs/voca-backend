package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.repository.EmailTokenRepository;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService {

    @Value("${front-server}")
    private String FRONT_SERVER;
    private final EmailSender emailSender;
    private final EmailTokenRepository emailTokenRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public UUID createEmailToken(String email) {
        Assert.notNull(email, "받는 이메일은 필수입니다.");

        Optional<EmailToken> byEmail = emailTokenRepository.findByEmail(email);
        EmailToken emailToken;
        if(byEmail.isPresent()) {
            emailToken = byEmail.get();
            emailToken.refreshEmailToken();
        } else {
            emailToken = EmailToken.createEmailToken(email);
            emailTokenRepository.save(emailToken);
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("회원가입 이메일 인증 메일");
        mailMessage.setText(FRONT_SERVER + "/email?token=" + emailToken.getToken());
        emailSender.sendEmail(mailMessage);

        return emailToken.getToken();
    }

    @Override
    public Optional<EmailToken> findByToken(String emailTokenId) {
        return emailTokenRepository
                .findByToken(UUID.fromString(emailTokenId));
    }

    @Override
    public Optional<EmailToken> findByEmail(String email) {
        return emailTokenRepository.findByEmail(email);
    }
}
