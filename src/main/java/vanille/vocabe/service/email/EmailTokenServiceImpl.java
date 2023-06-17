package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;

    @Override
    public EmailToken createEmailToken(String email) {
        Assert.notNull(email, "받는 이메일은 필수입니다.");

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

        return emailToken;
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

    @Override
    public boolean validateToken(EmailToken emailToken) {
        if(!emailToken.getExpirationDate().isBefore(LocalDateTime.now()) || emailToken.isExpired()) {
            throw new InvalidVerificationCodeException(ErrorCode.EXPIRED_TOKEN);
        }
        return true;
    }
}
