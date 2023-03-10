package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.VocabularyFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordQuerydslRepository;
import vanille.vocabe.repository.WordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {
    @Mock
    private WordRepository wordRepository;
    @Mock
    private VocabularyRepository vocabularyRepository;


    @InjectMocks
    private WordServiceImpl wordService;

    @DisplayName("[성공] 새로운 단어를 추가할 수 있다.")
    @Test
    void saveWord() throws IllegalAccessException {
        User user = UserFixture.getVerifiedUser();
        user.setIdForTest(1L);
        when(wordRepository.save(any(Word.class)))
                .thenReturn(WordFixture.get(user));

        Vocabulary voca = VocabularyFixture.getVocabularyFixture("test");
        voca.setCreatedByForTest(1L);
        when(vocabularyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(voca));

        WordDTO.NewWord request = WordDTO.NewWord.builder()
                .word("test-word1")
                .definition("definition")
                .user(user)
                .vocaId(1L)
                .build();

        Word word = wordService.saveWord(request);
        Assertions.assertEquals("mango", word.getWord());
        Assertions.assertEquals("망고", word.getDefinition());
        Assertions.assertEquals("mango is delicious", word.getNote());
    }

    @DisplayName("[실패] 자신이 만든 단어장이 아니라면 단어를 추가할 수 없다")
    @Test
    void saveWordFail() {
        User user = UserFixture.getVerifiedUser();
        user.setIdForTest(2L);

        Vocabulary voca = VocabularyFixture.getVocabularyFixture("test");
        voca.setCreatedByForTest(1L);
        when(vocabularyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(voca));

        WordDTO.NewWord request = WordDTO.NewWord.builder()
                .word("test-word1")
                .definition("definition")
                .user(user)
                .vocaId(1L)
                .build();

        Assertions.assertThrows(IllegalAccessException.class, () -> wordService.saveWord(request));
    }

    @DisplayName("[성공] 체크 상태를 바꾼다.")
    @Test
    void changeCheckStatus() {
        User user = mock(User.class);
        Word word = WordFixture.get(user);
        Word word2 = WordFixture.get(user);
        word2.changeCheckStatus();
        given(wordRepository.findById(1L)).willReturn(Optional.of(word));
        given(wordRepository.findById(2L)).willReturn(Optional.of(word2));

        wordService.changeCheck(1L);
        Assertions.assertTrue(word.isChecked());
        Assertions.assertEquals("망고", word.getDefinition());

        wordService.changeCheck(2L);
        Assertions.assertFalse(word2.isChecked());
        Assertions.assertEquals("망고", word2.getDefinition());
    }


}