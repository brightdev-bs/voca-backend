package vanille.vocabe.service;

import vanille.vocabe.payload.CommentDTO;

public interface CommentService {
    void createComment(CommentDTO.CommentForm form);
}
