package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Post;

public class PostDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostForm {
        Long communityId;
        String content;
        Long relatedPostId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetail {
        private Long id;
        private String content;
        private Long relatedPost;


        public static PostDetail from(Post post) {
            return PostDetail.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .relatedPost(post.getRelatedPost())
                    .build();
        }
    }
}
