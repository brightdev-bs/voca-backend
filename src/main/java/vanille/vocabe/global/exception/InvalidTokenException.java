package vanille.vocabe.global.exception;

import lombok.Getter;
import vanille.vocabe.global.constants.ErrorCode;

@Getter
public class InvalidTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
