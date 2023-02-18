package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndVerifiedTrue(String username);
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndVerifiedTrue(String email);

}
