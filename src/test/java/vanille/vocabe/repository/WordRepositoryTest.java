package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.config.jpaConfig.JpaConfig;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("소프트 삭제 테스트")
    @Test
    void softDelete() {
        Word word = wordRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        wordRepository.delete(word);
        entityManager.flush();
        Word deletedWord = wordRepository.findById(1L).get();
        Assertions.assertTrue(deletedWord.isDeleted());
    }

    @DisplayName("단어 조회")
    @Test
    void findByUserAndCreatedAtAndDeleted() {
        User user = userRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        Page<Word> response = wordRepository.findByUserAndCreatedAtAndDeleted(PageRequest.of(0, 10), user, LocalDate.of(2023, 9, 7), false);
        Assertions.assertEquals(3, response.getContent().size());
    }



}