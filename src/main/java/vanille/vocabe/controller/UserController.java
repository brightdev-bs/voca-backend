package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.service.UserService;
import vanille.vocabe.service.email.EmailService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ApiResponse signup(@RequestBody UserDTO.SignForm request) {
        userService.saveUser(request);
        return ApiResponse.of(HttpStatus.OK.toString(), request.getEmail());
    }

    @GetMapping("/sign-up")
    public ApiResponse confirmVerification(@RequestParam String token) throws Exception {
        log.info("token = {}", token);
        emailService.verifyEmail(token);
        return ApiResponse.of(HttpStatus.OK.toString(), "go");
    }

}
