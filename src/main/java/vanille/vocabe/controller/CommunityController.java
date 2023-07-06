package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.service.CommunityService;

import javax.mail.AuthenticationFailedException;
import javax.validation.Valid;

import java.util.List;

import static vanille.vocabe.payload.CommunityDTO.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/community")
    public ApiResponse getCommunities() {
        List<HomeResponse> communities = communityService.getCommunities();
        return ApiResponse.of(HttpStatus.OK.toString(), communities);
    }

    @GetMapping("/community/{id}")
    public ApiResponse getCommunityDetail(@PathVariable Long id) {
        CommunityDetail communityDetails = communityService.getCommunityDetails(id);
        return ApiResponse.of(HttpStatus.OK.toString(), communityDetails);
    }

    @PostMapping("/community/form")
    public ApiResponse createCommunity(@RequestBody @Valid CommunityForm form, @AuthenticationPrincipal final User user) throws AuthenticationFailedException {
        form.setUser(user);
        communityService.saveCommunity(form);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }

    @PostMapping("/community/{id}/members")
    public ApiResponse communityJoinRequest(@PathVariable Long id, @RequestBody @Valid JoinForm form, @AuthenticationPrincipal final User user) {
        form.setUser(user);
        form.setCommunityId(id);
        communityService.joinRequest(form);
        return ApiResponse.of(HttpStatus.OK.toString(), Constants.SUCCESS);
    }
}
