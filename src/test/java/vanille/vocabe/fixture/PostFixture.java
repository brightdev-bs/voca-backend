package vanille.vocabe.fixture;

import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Post;

public class PostFixture {

    public static Post getPostFixture(Community community) {
        return Post.builder()
                .community(community)
                .content("dummy content")
                .writer("vanille")
                .build();
    }
}
