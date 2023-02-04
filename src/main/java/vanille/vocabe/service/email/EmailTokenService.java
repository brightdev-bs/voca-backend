package vanille.vocabe.service.email;

import vanille.vocabe.entity.EmailToken;

public interface EmailTokenService {
    Long createEmailToken(String email);

    EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId);
}
