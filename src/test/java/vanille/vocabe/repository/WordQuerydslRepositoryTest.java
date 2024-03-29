package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static vanille.vocabe.global.Constants.VERIFIED_USER_EMAIL;

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
        User user = userRepository.findByEmail(VERIFIED_USER_EMAIL).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

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