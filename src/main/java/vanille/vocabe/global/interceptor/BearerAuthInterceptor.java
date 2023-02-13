package vanille.vocabe.global.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.InvalidTokenException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BearerAuthInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("Bearer Interceptor started");

        if (org.apache.commons.codec.binary.StringUtils.equals(request.getMethod(), "OPTIONS")) {
            return true;
        }

        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(token)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_TOKEN);
        }

        User userDetails;
        try {
            String username = JwtTokenUtils.getUsername(token, secretKey);
            userDetails = userService.findUserByUsername(username);

            if (!JwtTokenUtils.validate(token, userDetails.getUsername(), secretKey)) {
                log.error("Token is not valid {}", token);
                throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }


        setSession(userDetails);

        return true;
    }


    private String extractToken(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        while(headers.hasMoreElements()) {
            String value = headers.nextElement();
            if(value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
    private static void setSession(User userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, List.of(new SimpleGrantedAuthority(userDetails.getRole().toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
