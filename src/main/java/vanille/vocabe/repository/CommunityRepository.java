package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> { }
