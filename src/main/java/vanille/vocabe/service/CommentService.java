package vanille.vocabe.service;

import vanille.vocabe.entity.User;
import vanille.vocabe.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    void createComment(CommentDTO.CommentForm form, User user);

    List<CommentDTO.CommentDetail> getComments(Long postId);
}
