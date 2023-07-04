package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Comment;

public class CommentDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        private Long postId;
        private String commentContent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDetail {
        private Long id;
        private String content;
        private String writer;

        public static CommentDetail from(Comment comment) {
            return CommentDetail.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .writer(comment.getWriter())
                    .build();
        }
    }
}
