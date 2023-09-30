package vanille.vocabe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserWord;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.repository.UserWordRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserWordServiceImplTest {

    @Mock
    UserWordRepository userWordRepository;
    @InjectMocks
    UserWordServiceImpl userWordService;

    @DisplayName("[성공] UserWord 저장")
    @Test
    void saveUserWord() {
        User user = mock(User.class);
        Vocabulary voca = mock(Vocabulary.class);
        Word word = mock(Word.class);
        userWordService.save(user, voca, word);
        then(userWordRepository).should().save(any(UserWord.class));
    }

}