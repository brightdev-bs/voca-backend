package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Comment;
import vanille.vocabe.entity.Post;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.CommentDTO;
import vanille.vocabe.repository.CommentRepository;
import vanille.vocabe.repository.PostRepository;

import static vanille.vocabe.payload.CommentDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(CommentForm form) {
        Post post = postRepository.findById(form.getPostId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.builder()
                .post(post)
                .content(form.getContent())
                .build();

        commentRepository.save(comment);
    }

}
