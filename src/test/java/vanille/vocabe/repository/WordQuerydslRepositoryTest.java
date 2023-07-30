package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.global.config.JpaConfig;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WordQuerydslRepositoryTest {

    @Autowired
    private WordQuerydslRepository querydslRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    @Test
    void queryDslTest() {
        User user = UserFixture.getVerifiedUser();
        userRepository.saveAndFlush(user);

        wordRepository.saveAndFlush(WordFixture.get(user));
        wordRepository.saveAndFlush(WordFixture.get(user));
        wordRepository.saveAndFlush(WordFixture.get(user));
        wordRepository.saveAndFlush(WordFixture.get(user));

        LocalDate now = LocalDate.now();

        List<String> result = querydslRepository.findByUserAndCreatedAtBetweenAndGroupBy(user,
                now,
                LocalDate.of(now.getYear(), now.getMonth(), now.lengthOfMonth()));

        Assertions.assertEquals(1, result.size());
    }

}