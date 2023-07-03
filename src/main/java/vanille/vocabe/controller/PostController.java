package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.service.PostService;

import javax.mail.AuthenticationFailedException;
import java.util.List;
import java.util.stream.Collectors;

import static vanille.vocabe.payload.PostDTO.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping("/{communityId}/posts")
    public ApiResponse getPosts(@PathVariable Long communityId) {
        List<Post> posts = postService.getPosts(communityId);

        return ApiResponse.of(HttpStatus.CREATED.toString(),
                posts.stream().map(p -> PostDetail.from(p)).collect(Collectors.toList())
        );
    }

    @PostMapping("community/{communityId}/posts")
    public ApiResponse createPost(@PathVariable final Long communityId, @RequestBody PostForm form, @AuthenticationPrincipal User user) throws AuthenticationFailedException {
        form.setCommunityId(communityId);
        Long id = user.getId();
        log.info("user = {}", user);
        postService.createPost(form, id);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }
}
