package vanille.vocabe.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    // Todo : 실제 유저로 바꿔야 함.
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("vanille");
    }

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
