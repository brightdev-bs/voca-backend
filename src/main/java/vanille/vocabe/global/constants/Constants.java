package vanille.vocabe.global.constants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    public static final String CONFIRMATION_EMAIL = "이메일이 전송되었습니다.";
    public static final String EMAIL_VERIFICATION = "이메일 인증 완료되었습니다.";

    public static final long EMAIL_TOKEN_EXPIRATION_TIME = 5L;
}
