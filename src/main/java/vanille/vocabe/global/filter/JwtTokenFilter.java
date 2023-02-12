package vanille.vocabe.global.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token;
        try {
            if(!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
                log.error("Authorization Header doesn't start with Bearer {}", request.getRequestURI());
                chain.doFilter(request, response);
                return;
            } else {
                token = header.split(" ")[1].trim();
            }

            String username = JwtTokenUtils.getUsername(token, secretKey);
            User userDetails = userService.findUserByUsername(username);

            if (!JwtTokenUtils.validate(token, userDetails.getUsername(), secretKey)) {
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, List.of(new SimpleGrantedAuthority(userDetails.getRole().toString()))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (RuntimeException e) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

}
