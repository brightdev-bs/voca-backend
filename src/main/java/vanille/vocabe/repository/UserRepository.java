package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndVerifiedTrue(String email);

}
