package vanille.vocabe.fixture;

import vanille.vocabe.entity.Comment;
import vanille.vocabe.entity.Post;

public class CommentFixture {

    public static Comment getCommentFixture(Post post) {
        return Comment.builder()
                .content("Sfsdsf")
                .post(post)
                .writer("vanille")
                .build();
    }
}
