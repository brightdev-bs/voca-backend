package vanille.vocabe.global.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "이미 가입한 사용자입니다."),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 이름입니다."),

    NOT_FOUND_EMAIL_TOKEN(HttpStatus.BAD_REQUEST, "인증 코드가 없습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_WORD(HttpStatus.BAD_REQUEST, "단어를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 없습니다."),
    NOT_FOUND_VOCABULARY(HttpStatus.BAD_REQUEST, "단어장이 없습니다."),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 헤더 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),

    UNVERIFIED_USER(HttpStatus.BAD_REQUEST, "인증되지 않은 사용자입니다."),

    NO_AUTHORITY(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
