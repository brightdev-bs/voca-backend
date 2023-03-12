package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    List<Word> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDateTime);
    List<Word> findALLByVocabularyId(Long vocaId);

}
