package vanille.vocabe.global.exception;

import vanille.vocabe.global.constants.ErrorCode;

public class InvalidPasswordException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
