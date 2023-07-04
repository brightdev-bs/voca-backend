package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByCommunity_Id(Long id);
}
