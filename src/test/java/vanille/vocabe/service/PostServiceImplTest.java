package vanille.vocabe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vanille.vocabe.entity.*;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.PostRepository;
import vanille.vocabe.repository.UserRepository;

import javax.mail.AuthenticationFailedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static vanille.vocabe.payload.PostDTO.PostForm;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommunityRepository communityRepository;

    @InjectMocks
    PostServiceImpl postService;

    @DisplayName("포스트 생성")
    @Test
    void createPost() throws AuthenticationFailedException {
        User user = mock(User.class);
        Community community = mock(Community.class);
        CommunityUser communityUser = CommunityUser.builder()
                .community(community)
                .user(user)
                .build();

        PostForm form = PostForm.builder()
                .communityId(community.getId())
                .postContent("Adfsfsdff")
                .build();

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        given(community.getCommunityUsers()).willReturn(List.of(communityUser));

        postService.createPost(form, any(Long.class));
        then(postRepository).should().save(any(Post.class));
    }

    @DisplayName("포스트 리스트 조회")
    @Test
    void getPosts() {
        Community community = mock(Community.class);
        List<Post> posts = List.of(mock(Post.class), mock(Post.class), mock(Post.class), mock(Post.class));
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));
        given(community.getPosts()).willReturn(posts);

        assertEquals(4, postService.getPosts(community.getId(), mock(Pageable.class)).size());
    }

}