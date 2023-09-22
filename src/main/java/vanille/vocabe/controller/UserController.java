package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.service.UserService;
import vanille.vocabe.service.WordService;
import vanille.vocabe.service.email.EmailService;

import javax.validation.Valid;

import static vanille.vocabe.global.constants.Constants.EMAIL_VERIFICATION;
import static vanille.vocabe.payload.UserDTO.*;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final WordService wordService;

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private long expiredTime;

    @PostMapping("/v1/sign-up")
    public ApiResponse signup(@RequestBody @Valid SignForm request) {
        userService.saveUser(request);
        return ApiResponse.of(HttpStatus.OK.toString(), request.getEmail());
    }

    @PostMapping("/v1/email")
    public ApiResponse sendEmail(@RequestBody @Valid LoginForm request) {
        emailService.sendSignUpConfirmEmail(request.getEmail());
        return ApiResponse.of(HttpStatus.OK.toString(), request.getEmail());
    }

    @GetMapping("/v1/email")
    public ApiResponse confirmVerification(@RequestParam String token) throws Exception {
        emailService.verifyEmail(token);
        return ApiResponse.of(HttpStatus.OK.toString(), EMAIL_VERIFICATION);
    }

    @PostMapping("/v1/login")
    public ApiResponse login(@RequestBody LoginForm request) {
        User user = userService.login(request);
        String token = JwtTokenUtils.generateAccessToken(user.getUsername(), expiredTime, key);
        return ApiResponse.of(HttpStatus.OK.toString(), UserLoginResponse.from(user, token));
    }

    @GetMapping("/v1/my-page")
    public ApiResponse myPage(@AuthenticationPrincipal User user) {
        UserDetailWithStudyRecords userInfo = wordService.findPriorStudyRecords(user);
        return ApiResponse.of(HttpStatus.OK.toString(), userInfo);
    }

    @GetMapping("/v1/password")
    public ApiResponse findPassword(@RequestParam String email) {
        userService.findUserByEmail(email);
        emailService.sendPasswordFindEmail(email);
        return ApiResponse.of(HttpStatus.OK.toString(), email);
    }

    @PostMapping("/v1/password")
    public ApiResponse changePassword(@RequestBody @Valid PasswordForm form) {
        userService.changeUserPassword(form);
        return ApiResponse.of(HttpStatus.OK.toString(), true);
    }

}
