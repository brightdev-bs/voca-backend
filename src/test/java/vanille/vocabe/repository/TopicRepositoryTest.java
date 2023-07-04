package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;
import vanille.vocabe.global.config.JpaConfig;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicRepositoryTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    void createTopic() {
        Community community = Community.builder().name("Ddd").build();
        communityRepository.save(community);

        Topic topic = Topic.builder().community(community).build();
        topicRepository.save(topic);
        assertEquals(topic.getCommunity().getId(), community.getId());
    }

    @Test
    void findByCommunityId() {
        Community community = Community.builder().name("Ddd").build();
        communityRepository.save(community);

        Topic topic = Topic.builder().community(community).build();
        topicRepository.save(topic);

        Topic topicByCommunityId = topicRepository.findByCommunity_Id(community.getId());
        assertEquals(community.getId(), topicByCommunityId.getCommunity().getId());
    }
}