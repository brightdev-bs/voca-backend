package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.VocabularyFixture;
import vanille.vocabe.global.config.JpaConfig;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@DataJpaTest
class UserVocabularyRepositoryTest {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Test
    void getVocabularyByName() {
        Vocabulary test = VocabularyFixture.getVocabularyFixture("test");
        vocabularyRepository.save(test);

        Vocabulary findOne= vocabularyRepository.findByName("test").get();
        Assertions.assertEquals(test.getName(), findOne.getName());
        Assertions.assertEquals(test.getId(), findOne.getId());
    }

}