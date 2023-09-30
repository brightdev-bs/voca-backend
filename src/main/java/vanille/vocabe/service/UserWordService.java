package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserWord;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;

public interface UserWordService {
    UserWord save(User user, Vocabulary vocabulary, Word word);

    @Transactional
    UserWord createUserWordAndCheckStudied(User user, Word word);
}
