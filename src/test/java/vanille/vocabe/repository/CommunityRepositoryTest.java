package vanille.vocabe.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Community;
import vanille.vocabe.global.config.JpaConfig;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CommunityRepositoryTest {

    @Autowired
    CommunityRepository communityRepository;

    @DisplayName("커뮤니티 생성")
    @Test
    void createCommunity() {
        Community community = createCommunity("test community");
        Community save = communityRepository.save(community);
        assertEquals(community.getName(), save.getName());
        assertEquals(community.getDescription(), save.getDescription());
        assertEquals(community.isOpen(), save.isOpen());
    }

    @DisplayName("커뮤니티 검색")
    @Test
    void searchCommunity() {
        Community community = createCommunity("community1");
        Community community2 = createCommunity("comm2");
        Community community3 = createCommunity("commun3");
        communityRepository.save(community);
        communityRepository.save(community2);
        communityRepository.save(community3);

        assertEquals(3, communityRepository.findCommunitiesByNameContaining("com").size());
        assertEquals(1, communityRepository.findCommunitiesByNameContaining("comm2").size());
    }


    Community createCommunity(String name) {
        return Community.builder()
                .name(name)
                .description("community creating test")
                .open(true)
                .build();
    }
}