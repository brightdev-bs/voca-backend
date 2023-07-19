package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.service.PostService;

import javax.mail.AuthenticationFailedException;
import javax.validation.Valid;
import java.util.List;

import static vanille.vocabe.payload.PostDTO.PostDetail;
import static vanille.vocabe.payload.PostDTO.PostForm;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/community/{communityId}/posts/form")
    public ApiResponse createPost(@PathVariable final Long communityId, @RequestBody @Valid PostForm form, @AuthenticationPrincipal User user) throws AuthenticationFailedException {
        form.setCommunityId(communityId);
        Long id = user.getId();
        postService.createPost(form, id);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }

    @GetMapping("/community/{communityId}/posts")
    public ApiResponse getPosts(@PathVariable Long communityId, Pageable pageable) {
        List<PostDetail> posts = postService.getPosts(communityId, pageable);
        return ApiResponse.of(HttpStatus.OK.toString(), posts);
    }
}
