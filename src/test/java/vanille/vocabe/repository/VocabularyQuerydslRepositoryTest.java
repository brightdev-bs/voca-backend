package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vanille.vocabe.entity.Vocabulary;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class VocabularyQuerydslRepositoryTest {

    @Autowired
    private VocabularyQuerydslRepository vocabularyRepository;

    @DisplayName("좋아요 많은 공개 단어장 5개 가져오기")
    @Test
    public void getPublicVocabularyLimit() {
        List<Vocabulary> publicVocabulariesLimitFive = vocabularyRepository.findPublicVocabulariesLimitFive();
        Assertions.assertEquals(5, publicVocabulariesLimitFive.size());
        Assertions.assertEquals("First Favorite", publicVocabulariesLimitFive.get(0).getName());
        Assertions.assertEquals(30, publicVocabulariesLimitFive.get(0).getLiked());
        Assertions.assertEquals("Mix Favorite", publicVocabulariesLimitFive.get(1).getName());
        Assertions.assertEquals(29, publicVocabulariesLimitFive.get(1).getLiked());
    }


}