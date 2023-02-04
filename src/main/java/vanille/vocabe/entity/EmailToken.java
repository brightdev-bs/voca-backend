package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class EmailToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME = 5L;

    @Id
    @GeneratedValue(generator = "uuid")
    private Long id;

    private LocalDateTime expirationDate;
    private boolean expired;
    private String email;

    public static EmailToken createEmailToken(String email) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME);
        emailToken.expired = false;
        emailToken.email = email;
        return emailToken;
    }

    public void setExpired() {
        this.expired = true;
    }
}
