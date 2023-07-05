package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.*;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.PostRepository;
import vanille.vocabe.repository.TopicRepository;
import vanille.vocabe.repository.UserRepository;

import javax.mail.AuthenticationFailedException;
import java.util.ArrayList;
import java.util.List;

import static vanille.vocabe.payload.PostDTO.*;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;


    @Transactional
    @Override
    public void createPost(PostForm form, final Long userId) throws AuthenticationFailedException {
        Long communityId = form.getCommunityId();
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        List<CommunityUser> communityUsers = community.getCommunityUsers();
        if(isCommunityMember(user, communityUsers)) {
            Post post = Post.builder()
                    .community(community)
                    .content(form.getPostContent())
                    .build();
            postRepository.save(post);
        } else {
            throw new AuthenticationFailedException(ErrorCode.NO_AUTHORITY.toString());
        }
    }

    private boolean isCommunityMember(User user, List<CommunityUser> communityUsers) {
        for (CommunityUser communityUser : communityUsers) {
            Long communityUserId = communityUser.getUser().getId();
            if(user.getId().equals(communityUserId)) return true;
        }
        return false;
    }

    @Override
    public List<PostDetail> getPosts(Long communityId, Long topicId) {
        communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TOPIC));

        List<Post> posts = topic.getPosts();
        // Todo : 쿼리가 N + 1개 나갈 것임. 따라서 성능 문제 생기면 수정해야함.
        List<PostDetail> response = new ArrayList<>();
        for(Post p : posts) {
            Long writerId = p.getCreatedBy();
            User user = userRepository.findById(writerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
            response.add(PostDetail.from(p, user.getUsername()));
        }
        return response;
    }
}
