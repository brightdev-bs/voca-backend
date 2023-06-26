package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;
import vanille.vocabe.payload.UserDTO;

public class UserFixture {

    public static final String email = "vanille@gmail.com";
    public static User getVerifiedUser() {
        return User.of("test", email, "{bcrypt}1kdasdfwcv", true);
    }

    public static User getVerifiedUser(String username) {
        return User.of(username, email, "1kdasdfwcv", true);
    }

    public static User getUnverifiedUser() {

        return User.of("test", email, "1kdasdfwcv");
    }
}
