package vanille.vocabe.service;

import vanille.vocabe.payload.PostDTO;

import javax.mail.AuthenticationFailedException;
import java.util.List;

public interface PostService {

    void createPost(PostDTO.PostForm form, Long userId) throws AuthenticationFailedException;


    List<PostDTO.PostDetail> getPosts(Long communityId, Long topicId);
}
