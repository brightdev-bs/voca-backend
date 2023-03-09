package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Vocabulary;

import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    Optional<Vocabulary> findByName(String name);
}
