package vanille.vocabe.global.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "It's is already sign-up"),
    DUPLICATED_REQUEST(HttpStatus.BAD_REQUEST, "It's already requested"),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "It's already being used"),

    NOT_FOUND_EMAIL_TOKEN(HttpStatus.BAD_REQUEST, "There is no verfication code"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "We couldn't find the user"),
    NOT_FOUND_WORD(HttpStatus.BAD_REQUEST, "We couldn't find the word"),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "There is no token"),
    NOT_FOUND_VOCABULARY(HttpStatus.BAD_REQUEST, "There is no vocabulary"),
    NOT_FOUND_COMMUNITY(HttpStatus.BAD_REQUEST, "Doesn't exist community"),
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "Doesn't exist post"),
    NOT_FOUND_TOPIC(HttpStatus.BAD_REQUEST, "Doesn't exist topic"),
    NOT_FOUND_APPLICANT(HttpStatus.BAD_REQUEST, "Doesn't exist applicant"),

    SOCIAL_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "You sign-up with social platform"),
    SOCIAL_NOT_CONNECT(HttpStatus.BAD_REQUEST, "You didn't sign-up with social platform"),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Password isn't correct"),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "Wrong verification code"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Wrong header token"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "Expired token"),

    UNVERIFIED_USER(HttpStatus.BAD_REQUEST, "This account need to be certificated. Check ur email"),

    NO_AUTHORITY(HttpStatus.BAD_REQUEST, "You don't have the authority"),
    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
