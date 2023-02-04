package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.global.config.JpaConfig;

import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("인증된 사용자를 찾아 온다.")
    @Test
    void findByEmailAndVerifiedTrue() {
        String email = "vanille@gmail.com";
        User user = UserFixture.getVerifiedUser();
        userRepository.saveAndFlush(user);

        User findUser = userRepository.findByEmailAndVerifiedTrue(email).get();
        Assertions.assertEquals(findUser.getEmail(), email);
        Assertions.assertTrue(findUser.isVerified());
    }

    @DisplayName("인증되지 않은 사용자는 찾아 오지 않는다.")
    @Test
    void notFoundIfUnverified() {
        String email = "vanille@gmail.com";
        User user = UserFixture.getUnverifiedUser();
        userRepository.saveAndFlush(user);

        Optional<User> findUser = userRepository.findByEmailAndVerifiedTrue(email);
        Assertions.assertThrows(NoSuchElementException.class, findUser::get);
    }

}