package vanille.vocabe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    Page<Word> findByUserAndCreatedAtAndDeleted(Pageable pageable, User user, LocalDate createdAt, Boolean Deleted);
    Page<Word> findALLByVocabularyId(Pageable pageable, Long vocaId);

}
