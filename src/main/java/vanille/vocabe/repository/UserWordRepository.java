package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserWord;
import vanille.vocabe.entity.Word;

import java.util.Optional;

public interface UserWordRepository extends JpaRepository<UserWord, Long> {

    Optional<UserWord> findUserWordByUserAndWord(User user, Word word);
}
