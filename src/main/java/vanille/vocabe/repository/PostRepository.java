package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findTop10ByCommunity_IdOrderByCreatedAtDesc(Long id);
}
