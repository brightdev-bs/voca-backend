package vanille.vocabe.service.email;

import vanille.vocabe.entity.EmailToken;

import java.util.Optional;
import java.util.UUID;

public interface EmailTokenService {
    UUID createEmailToken(String email);

    Optional<EmailToken> findByToken(String emailTokenId);

    Optional<EmailToken> findByEmail(String email);
}
