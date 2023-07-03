package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.CommunityDTO;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.service.CommunityService;

import javax.validation.Valid;

import static vanille.vocabe.payload.CommunityDTO.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/community")
    public ApiResponse createCommunity(@RequestBody @Valid CommunityForm form, @AuthenticationPrincipal final User user) {
        form.setUser(user);
        communityService.saveCommunity(form);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }
}
