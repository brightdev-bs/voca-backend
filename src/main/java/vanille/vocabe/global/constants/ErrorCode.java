package vanille.vocabe.global.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),

    NOT_FOUND_EMAIL_TOKEN(HttpStatus.BAD_REQUEST, "인증 정보를 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.")

    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
