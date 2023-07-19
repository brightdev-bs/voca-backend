package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.CommentDTO;
import vanille.vocabe.service.CommentService;

import javax.mail.AuthenticationFailedException;
import javax.validation.Valid;

import java.util.List;

import static vanille.vocabe.payload.CommentDTO.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{id}/comments/form")
    public ApiResponse createComment(@PathVariable Long id, @RequestBody @Valid CommentForm form, @AuthenticationPrincipal User user) {
        form.setPostId(id);
        commentService.createComment(form, user);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }

    @GetMapping("/posts/{id}/comments")
    public ApiResponse getComments(@PathVariable Long id) {
        List<CommentDetail> comments = commentService.getComments(id);
        return ApiResponse.of(HttpStatus.OK.toString(), comments);
    }

}
