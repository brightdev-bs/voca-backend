package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.EmailToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EmailTokenRepository extends JpaRepository<EmailToken, UUID> {

    Optional<EmailToken> findByIdAndExpirationDateAfterAndExpired(UUID emailTokenId, LocalDateTime now, boolean expired);

    Optional<EmailToken> findByEmail(String email);
}
