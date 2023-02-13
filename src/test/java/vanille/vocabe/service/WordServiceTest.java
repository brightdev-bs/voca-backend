package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.WordRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {
    @Mock
    private WordRepository wordRepository;
    @InjectMocks
    private WordServiceImpl wordService;

    @DisplayName("[성공] 새로운 단어를 추가할 수 있다.")
    @Test
    void saveWord() {
        when(wordRepository.save(any(Word.class)))
                .thenReturn(WordFixture.get(UserFixture.getVerifiedUser()));

        Word word = wordService.saveWord(mock(WordDTO.NewWord.class));
        Assertions.assertEquals("mango", word.getWord());
        Assertions.assertEquals("망고", word.getDefinition());
        Assertions.assertEquals("mango is delicious", word.getNote());
    }

    // Todo : createdAt에 따른 리스트 조회 테스트 작성

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