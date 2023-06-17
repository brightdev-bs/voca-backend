package vanille.vocabe.entity;

import lombok.Getter;

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

    private EmailToken() {}

    public static EmailToken createEmailToken(String email) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME);
        emailToken.expired = false;
        emailToken.email = email;
        emailToken.token = UUID.randomUUID();
        return emailToken;
    }

    public void setExpired(boolean flag) {
        this.expired = flag;
    }

    public void refreshEmailToken() {
        this.token = UUID.randomUUID();
        this.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME);
        this.expired = false;
    }

    public static EmailToken createFixtureForTest(String email, LocalDateTime time) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = time;
        emailToken.expired = false;
        emailToken.email = email;
        emailToken.token = UUID.randomUUID();
        return emailToken;
    }


    public void plusExpirationTimeForTest(Long hours) {
        this.expirationDate = LocalDateTime.now().plusHours(hours);
    }
}
