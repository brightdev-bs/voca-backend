package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;
import vanille.vocabe.payload.UserDTO;

public class UserFixture {

    public static final String email = "vanille@gmail.com";
    public static final String email2 = "vanille2@gmail.com";
    public static User getVerifiedUser() {
        return User.of("test", email, "{bcrypt}1kdasdfwcv", true);
    }

    public static User getVerifiedUser(String username) {
        return User.of(username, email2, "{bcrypt}1kdasdfwcv", true);
    }

    public static User getVerifiedUser(String username, String email) {
        return User.of(username, email, "{bcrypt}1kdasdfwcv", true);
    }

    public static User getUnverifiedUser() {

        return User.of("test", email, "{bcrypt}1kdasdfwcv", false);
    }
}
