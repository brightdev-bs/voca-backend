package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.CommunityUser;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
}
