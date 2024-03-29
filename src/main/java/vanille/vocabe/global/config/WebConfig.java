package vanille.vocabe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${front-server}")
    private String FRONT_SERVER;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONT_SERVER, FRONT_SERVER + ":8089")
                .allowedOriginPatterns("https://voca-world.com")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                );
    }
}
