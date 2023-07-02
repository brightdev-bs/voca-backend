package vanille.vocabe.payload;

import lombok.Builder;
import lombok.Data;
import vanille.vocabe.entity.Community;

public class PostDTO {

    @Data
    @Builder
    public static class PostForm {
        Long communityId;
        String content;
        Long relatedPostId;
    }
}
