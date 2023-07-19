package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Comment;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.PostFixture;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.payload.CommentDTO;
import vanille.vocabe.payload.CommentDTO.CommentForm;
import vanille.vocabe.repository.CommentRepository;
import vanille.vocabe.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @DisplayName("댓글 생성하기")
    @Test
    void createComment() {
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(mock(Post.class)));

        commentService.createComment(mock(CommentForm.class), mock(User.class));

        then(commentRepository).should().save(any(Comment.class));
    }

    @DisplayName("댓글 조회하기")
    @Test
    void getComments() {
        Post post = mock(Post.class);
        Comment comment = mock(Comment.class);
        Comment comment2 = mock(Comment.class);
        Comment comment3 = mock(Comment.class);
        post.addComment(comment);
        post.addComment(comment2);
        post.addComment(comment3);


        List<Comment> comments = List.of(comment, comment2, comment3);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(post.getComments()).willReturn(comments);

        List<CommentDTO.CommentDetail> response = commentService.getComments(post.getId());
        Assertions.assertEquals(3, response.size());

    }



}