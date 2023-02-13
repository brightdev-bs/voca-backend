package vanille.vocabe.global.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),

    NOT_FOUND_EMAIL_TOKEN(HttpStatus.BAD_REQUEST, "인증 정보를 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_WORD(HttpStatus.BAD_REQUEST, "단어를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 없습니다."),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 코드입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    UNVERIFIED_USER(HttpStatus.BAD_REQUEST, "인증되지 않은 사용자입니다."),

    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
