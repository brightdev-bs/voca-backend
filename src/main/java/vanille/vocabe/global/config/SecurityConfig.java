package vanille.vocabe.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vanille.vocabe.global.filter.JwtTokenFilter;
import vanille.vocabe.service.UserService;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll().and().addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class))
                .build();
    }

}
