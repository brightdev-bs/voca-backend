package vanille.vocabe.payload;

import lombok.Builder;
import lombok.Data;
import vanille.vocabe.entity.User;

public class CommentDTO {

    @Data
    @Builder
    public static class CommentForm {
        private Long postId;
        private String content;
    }
}
