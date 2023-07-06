package vanille.vocabe.fixture;

import vanille.vocabe.entity.Community;

public class CommunityFixture {

    public static Community getCommunityFixture() {
        return Community.builder()
                .name("test")
                .description("test")
                .open(true)
                .build();
    }
}
