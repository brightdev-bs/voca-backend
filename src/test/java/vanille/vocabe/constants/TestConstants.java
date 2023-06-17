package vanille.vocabe.constants;

import static vanille.vocabe.global.util.JwtTokenUtils.generateAccessToken;

public class TestConstants {

    public static final String BEARER_TOKEN = getBearerToken();

    public static final String TEST_EMAIL = "brightbs.it@naver.com";

    private static String getBearerToken() {
        return generateAccessToken("test", 2592000000L, "voca-backend-secretkey-application-yml-local");
    }
}
