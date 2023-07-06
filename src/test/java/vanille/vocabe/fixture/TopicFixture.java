package vanille.vocabe.fixture;

import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;

public class TopicFixture {

    public static Topic getTopicFixture(Community community) {
        return Topic.builder()
                .content("test")
                .community(community)
                .build();
    }
}
