package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static vanille.vocabe.global.constants.Constants.EMAIL_TOKEN_EXPIRATION_TIME;

@Getter
@Entity
public class EmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BINARY(16)")
    private UUID token;

    private LocalDateTime expirationDate;
    private boolean expired;
    private String email;

    public static EmailToken createEmailToken(String email) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME);
        emailToken.expired = false;
        emailToken.email = email;
        emailToken.token = UUID.randomUUID();
        return emailToken;
    }

    public void setExpired() {
        this.expired = true;
    }

    public void refreshEmailToken() {
        this.token = UUID.randomUUID();
        this.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME);
    }

    public static EmailToken createFixtureForTest(String email, LocalDateTime time) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = time;
        emailToken.expired = false;
        emailToken.email = email;
        emailToken.token = UUID.randomUUID();
        return emailToken;
    }


    public void changeExpirationTimeForTest(Long hours) {
        this.expirationDate = LocalDateTime.now().plusHours(hours);
    }
}
