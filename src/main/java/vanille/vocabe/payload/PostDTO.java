package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Comment;
import vanille.vocabe.entity.Post;
import vanille.vocabe.payload.CommentDTO.CommentDetail;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostForm {
        Long communityId;
        @NotBlank
        String postContent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetail {
        private Long id;
        private String content;
        private List<CommentDetail> comments;
        private String writer;


        public static PostDetail from(Post post, String username) {
            List<CommentDetail> comments = post.getComments().stream().map(PostDetail::getCommentDetail).collect(Collectors.toList());
            return PostDetail.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .comments(comments)
                    .writer(username)
                    .build();
        }

        private static CommentDetail getCommentDetail(Comment c) {
            return CommentDetail.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .writer(c.getWriter())
                    .build();

        }
    }
}
