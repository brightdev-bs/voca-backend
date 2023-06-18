package vanille.vocabe.fixture;

import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;

import java.time.LocalDateTime;

import static vanille.vocabe.constants.TestConstants.TEST_EMAIL;

public class EmailTokenFixture {

    public static EmailToken createEmailToken() {
        return EmailToken.createEmailToken(TEST_EMAIL);
    }

    public static EmailToken createExpiredDateEmailToken() {
        return EmailToken.createFixtureForTest(TEST_EMAIL, LocalDateTime.of(1998,4,6,13,00));
    }
}
