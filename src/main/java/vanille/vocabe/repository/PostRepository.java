package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
