package vanille.vocabe.global.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vanille.vocabe.entity.User;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if(authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getId());
    }
}
