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
        Community community = Community.builder()
                .name("test community")
                .description("community creating test")
                .open(true)
                .build();
        Community save = communityRepository.save(community);
        assertEquals( community.getName(), save.getName());
        assertEquals(community.getDescription(), save.getDescription());
        assertEquals(community.isOpen(), save.isOpen());
    }

}