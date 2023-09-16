package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;
    private final EmailSender emailSender;
    @Value("${front-server}")
    public String FRONT_SERVER;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public EmailToken createEmailToken(String email) {

        Assert.notNull(email, "받는 이메일은 필수입니다.");
        log.error("createEmailToken");

        Optional<EmailToken> byEmail = emailTokenRepository.findByEmail(email);

        EmailToken emailToken;
        if(byEmail.isPresent()) {
            log.debug("{}는 존재하는 이메일로 토큰을 갱신합니다.", byEmail.get().getEmail());
            emailToken = byEmail.get();
            emailToken.refreshEmailToken();
        } else {
            emailToken = EmailToken.createEmailToken(email);
        }

        emailTokenRepository.save(emailToken);
        final String SIGN_UP_MAIL_SUBJECT = "voca-world signup confirm";
        sendEmail(emailToken, SIGN_UP_MAIL_SUBJECT, "/email?token=");

        return emailToken;
    }

    /**
     *
     * @param emailToken
     * @param subject
     * @param path 쿼리 파라미터의 경우 '?[name]='추가할 것. /example?token=
     */
    public void sendEmail(EmailToken emailToken, String subject, String path) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailToken.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(FRONT_SERVER + path + emailToken.getToken());
        emailSender.sendEmail(mailMessage);
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

    @Transactional
    @Override
    public boolean validateToken(EmailToken emailToken) {
        if(emailToken.getExpirationDate().isBefore(LocalDateTime.now()) || emailToken.isExpired()) {
            throw new InvalidVerificationCodeException(ErrorCode.EXPIRED_TOKEN);
        }
        emailToken.setExpired(true);
        return true;
    }
}
