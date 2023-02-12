package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.service.UserService;
import vanille.vocabe.service.email.EmailService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static vanille.vocabe.global.constants.Constants.EMAIL_VERIFICATION;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private long expiredTime;

    @PostMapping("/sign-up")
    public ApiResponse signup(@RequestBody @Valid UserDTO.SignForm request) {
        userService.saveUser(request);
        return ApiResponse.of(HttpStatus.OK.toString(), request.getEmail());
    }

    @GetMapping("/sign-up")
    public ApiResponse confirmVerification(@RequestParam String token) throws Exception {
        emailService.verifyEmail(token);
        return ApiResponse.of(HttpStatus.OK.toString(), EMAIL_VERIFICATION);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody UserDTO.loginForm request) {
        User user = userService.login(request);
        String token = JwtTokenUtils.generateAccessToken(user.getUsername(), expiredTime, key);
        return ApiResponse.of(HttpStatus.OK.toString(), UserDTO.UserResponse.from(user, token));
    }

}
