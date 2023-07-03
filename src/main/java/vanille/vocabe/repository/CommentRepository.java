package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
