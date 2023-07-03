package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Community;

import java.awt.print.Pageable;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findAll();
    List<Community> findCommunitiesByNameContaining(String str);
}
