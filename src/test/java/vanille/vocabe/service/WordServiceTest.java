package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.VocabularyFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordQuerydslRepository;
import vanille.vocabe.repository.WordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {
    @Mock
    private WordRepository wordRepository;
    @Mock
    private WordQuerydslRepository wordQuerydslRepository;
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
                .date(LocalDate.now().toString())
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
        word2.changeStudyFlag();
        given(wordRepository.findById(1L)).willReturn(Optional.of(word));
        given(wordRepository.findById(2L)).willReturn(Optional.of(word2));

        wordService.changeStudiedFlag(1L, user);
        Assertions.assertTrue(word.isStudied());
        Assertions.assertEquals("망고", word.getDefinition());

        wordService.changeStudiedFlag(2L, user);
        Assertions.assertFalse(word2.isStudied());
        Assertions.assertEquals("망고", word2.getDefinition());
    }

    @DisplayName("공부한 날짜 기록을 가져온다.")
    @Test
    void getStudyRecords() {
        User user = UserFixture.getVerifiedUser();
        user.setCreatedAtForTest(LocalDateTime.of(2023, 01, 01, 00, 00));
        LocalDate now = LocalDate.now();
        List<String> dates = List.of("01/01/2023", "01/02/2023", "02/02/2023", "03/01/2023", "04/01/2023");
        given(wordQuerydslRepository.findByUserAndCreatedAtBetweenAndGroupBy(
                user,
                user.getCreatedAt().toLocalDate(),
                LocalDate.of(now.getYear(), now.getMonth(), now.lengthOfMonth()))
        ).willReturn(dates);

        UserDTO.UserDetailWithStudyRecords priorStudyRecords = wordService.findPriorStudyRecords(user);
        Assertions.assertEquals(5, priorStudyRecords.getDates().size());
    }

    @DisplayName("[성공] 단어를 삭제한다.")
    @Test
    void deleteWord() throws IllegalAccessException {
        Word word = mock(Word.class);
        User user = mock(User.class);
        given(wordRepository.findById(any(Long.class))).willReturn(Optional.of(word));
        given(word.getCreatedBy()).willReturn(1L);
        given(user.getId()).willReturn(1L);
        wordService.deleteWord(any(Long.class), user);
        then(wordRepository).should().delete(word);
    }

    @DisplayName("[실패] 단어를 만든 사람이 아닐 때 삭제할 수 없다.")
    @Test
    void deleteWordFail() throws IllegalAccessException {
        Word word = mock(Word.class);
        User user = mock(User.class);
        given(wordRepository.findById(any(Long.class))).willReturn(Optional.of(word));
        given(word.getCreatedBy()).willReturn(1L);
        given(user.getId()).willReturn(2L);
        Assertions.assertThrows(IllegalAccessException.class, () -> wordService.deleteWord(any(Long.class), user));
    }
}