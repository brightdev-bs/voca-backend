package vanille.vocabe.constants;

import static vanille.vocabe.global.Constants.VERIFIED_USER_EMAIL;
import static vanille.vocabe.global.util.JwtTokenUtils.generateAccessToken;

public class TestConstants {

    public static final String BEARER_TOKEN = getBearerToken();

    public static final String TEST_EMAIL = VERIFIED_USER_EMAIL;

    private static String getBearerToken() {
        return generateAccessToken("verifiedUser", 2592000000L, "voca-backend-secretkey-application-yml-local");
    }
}
