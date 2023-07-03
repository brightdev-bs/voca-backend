package vanille.vocabe.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vanille.vocabe.global.interceptor.BearerAuthInterceptor;

@Slf4j
@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {

    private final BearerAuthInterceptor bearerAuthInterceptor;

    public JwtInterceptorConfig(BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor)
                .addPathPatterns("/api/v1/words")
                .addPathPatterns("/api/v1/my-page")
                .addPathPatterns("/api/v1/voca/**")
                .addPathPatterns("/api/v1/community/form")
                .addPathPatterns("/api/v1/community/{id}/posts")
                .addPathPatterns("/api/v1/posts/{id}/comments");
    }
}
