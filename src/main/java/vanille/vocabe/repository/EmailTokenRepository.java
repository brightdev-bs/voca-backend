package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.EmailToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByToken(UUID emailTokenId);

    Optional<EmailToken> findByEmail(String email);
}
