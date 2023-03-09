package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Vocabulary;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
}
