package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.config.jpaConfig.JpaConfig;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

import static vanille.vocabe.global.Constants.UNVERIFIED_USER_EMAIL;
import static vanille.vocabe.global.Constants.VERIFIED_USER_EMAIL;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("인증된 사용자를 찾아 온다.")
    @Test
    void findByEmailAndVerifiedTrue() {
        final String email = VERIFIED_USER_EMAIL;
        User findUser = userRepository.findByEmailAndVerifiedTrue(email).get();
        Assertions.assertEquals(findUser.getEmail(), email);
        Assertions.assertTrue(findUser.isVerified());
    }

    @DisplayName("인증되지 않은 사용자는 찾아 오지 않는다.")
    @Test
    void notFoundIfUnverified() {
        final String email = UNVERIFIED_USER_EMAIL;
        Optional<User> findUser = userRepository.findByEmailAndVerifiedTrue(email);
        Assertions.assertThrows(NoSuchElementException.class, findUser::get);
    }

    @DisplayName("사용자 이름으로 사용자를 찾는다. - 인증된 사용자")
    @Test
    void findByUsername() {
        String username = "verifiedUser";
        User findUser = userRepository.findByUsernameAndVerifiedTrue(username).get();
        Assertions.assertEquals(VERIFIED_USER_EMAIL, findUser.getEmail());
        Assertions.assertEquals(username, findUser.getUsername());
    }

    @DisplayName("사용자 이름으로 사용자를 찾는다. - 인증되지 않은 사용자")
    @Test
    void findByUsernameFail() {
        final String username = "unverifiedUser";

        Assertions.assertThrows(NotFoundException.class,
                () -> userRepository.findByUsernameAndVerifiedTrue(username)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER)));
    }

}