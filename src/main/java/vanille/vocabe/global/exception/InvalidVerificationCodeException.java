package vanille.vocabe.global.exception;

import lombok.Getter;
import vanille.vocabe.global.constants.ErrorCode;

@Getter
public class InvalidVerificationCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidVerificationCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
