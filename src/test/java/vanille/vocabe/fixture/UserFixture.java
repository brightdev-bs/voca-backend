package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;

public class UserFixture {

    public static User getVerifiedUser() {
        return User.of("vanille", "vanille@gmail.com", "1kdasdfwcv", true);
    }

    public static User getUnverifiedUser() {

        return User.of("vanille", "vanille@gmail.com", "1kdasdfwcv");
    }
}
