package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserVocabulary;
import vanille.vocabe.entity.Vocabulary;

import java.util.Optional;

public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long> {
    Optional<UserVocabulary> findUserVocabularyByUserAndVocabulary(User user, Vocabulary voca);
}
