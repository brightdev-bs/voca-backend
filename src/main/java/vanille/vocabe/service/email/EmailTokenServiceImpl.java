package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final EmailTokenRepository emailTokenRepository;

    @Override
    public EmailToken createEmailToken(String email) {
        Assert.notNull(email, "받는 이메일은 필수입니다.");

        Optional<EmailToken> byEmail = emailTokenRepository.findByEmail(email);
        EmailToken emailToken;
        if(byEmail.isPresent()) {
            emailToken = byEmail.get();
            emailToken.refreshEmailToken();
            emailToken.setExpired(false);
        } else {
            emailToken = EmailToken.createEmailToken(email);
            emailTokenRepository.save(emailToken);
        }

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
}
