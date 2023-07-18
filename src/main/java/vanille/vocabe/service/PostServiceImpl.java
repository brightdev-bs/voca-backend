package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.CommunityUser;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.PostRepository;
import vanille.vocabe.repository.UserRepository;

import javax.mail.AuthenticationFailedException;
import java.util.List;
import java.util.stream.Collectors;

import static vanille.vocabe.payload.PostDTO.PostDetail;
import static vanille.vocabe.payload.PostDTO.PostForm;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;


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
                    .writer(user.getUsername())
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
    public List<PostDetail> getPosts(Long communityId, Pageable pageable) {
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        return community.getPosts().stream().map(PostDetail::from).collect(Collectors.toList());
    }
}
