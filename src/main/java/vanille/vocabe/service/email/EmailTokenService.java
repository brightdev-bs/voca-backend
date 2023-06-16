package vanille.vocabe.service.email;

import vanille.vocabe.entity.EmailToken;

import java.util.Optional;

public interface EmailTokenService {
    EmailToken createEmailToken(String email);

    Optional<EmailToken> findByToken(String emailTokenId);

    Optional<EmailToken> findByEmail(String email);
}
