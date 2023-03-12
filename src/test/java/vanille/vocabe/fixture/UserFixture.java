package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;
import vanille.vocabe.payload.UserDTO;

public class UserFixture {

    public static User getVerifiedUser() {
        return User.of("test", "vanille@gmail.com", "{bcrypt}1kdasdfwcv", true);
    }

    public static User getVerifiedUser(String username) {
        return User.of(username, "vanille@gmail.com", "{bcrypt}1kdasdfwcv", true);
    }

    public static User getUnverifiedUser() {

        return User.of("test", "vanille@gmail.com", "1kdasdfwcv");
    }
}
