package vanille.vocabe.service;

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
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.payload.CommentDTO;
import vanille.vocabe.payload.CommentDTO.CommentForm;
import vanille.vocabe.repository.CommentRepository;
import vanille.vocabe.repository.PostRepository;

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



}