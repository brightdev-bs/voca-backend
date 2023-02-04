package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.EmailToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByIdAndExpirationDateAfterAndExpired(String emailTokenId, LocalDateTime now, boolean expired);
}
