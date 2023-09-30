package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserWord;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.repository.UserWordRepository;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserWordServiceImpl implements UserWordService {

    private final UserWordRepository userWordRepository;

    @Transactional
    @Override
    public UserWord save(User user, Vocabulary vocabulary, Word word) {
        UserWord userWord = UserWord.of(user, vocabulary, word);
        return userWordRepository.save(userWord);
    }

    @Transactional
    @Override
    public UserWord createUserWordAndCheckStudied(User user, Word word) {
        Optional<UserWord> optionalUserWord = userWordRepository.findUserWordByUserAndWord(user, word);
        UserWord userWord = null;
        if(optionalUserWord.isEmpty()) {
            userWord = UserWord.of(user, word.getVocabulary(), word);
            userWord.studiedTrue();
        } else {
            userWord = optionalUserWord.get();
            userWord.studiedTrue();
        }
        return userWordRepository.save(userWord);
    }
}
