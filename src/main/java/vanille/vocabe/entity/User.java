package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;
import vanille.vocabe.payload.UserDTO;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean verified;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {}

    protected User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }

    protected User(String username, String email, String password, boolean verified) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.role = UserRole.USER;
    }

    public static User of(String username, String email, String password) {
        return new User(username, email, password);
    }

    public static User of(String username, String email, String password, boolean verified) {
        return new User(username, email, password, verified);
    }

    public static User from(UserDTO.SignForm form) {
        return new User(form.getUsername(), form.getEmail(), form.getPassword());
    }

    public void changeUsernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setVerified() {
        this.verified = true;
    }

    public void setPremiumRole() {
        this.role = UserRole.PREMIUM;
    }

}
