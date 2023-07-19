package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Comment;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.CommentRepository;
import vanille.vocabe.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

import static vanille.vocabe.payload.CommentDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void createComment(CommentForm form, User user) {
        Post post = postRepository.findById(form.getPostId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.builder()
                .post(post)
                .content(form.getCommentContent())
                .writer(user.getUsername())
                .build();

        commentRepository.save(comment);
    }

    @Override
    public List<CommentDetail> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
        return post.getComments().stream().map(CommentDetail::from).collect(Collectors.toList());
    }

}
