package vanille.vocabe.service;

import vanille.vocabe.payload.PostDTO;

import javax.mail.AuthenticationFailedException;

public interface PostService {

    void createPost(PostDTO.PostForm form, Long userId) throws AuthenticationFailedException;
}
