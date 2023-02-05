package vanille.vocabe.global.exception;

import vanille.vocabe.global.constants.ErrorCode;

public class UnverifiedEmailException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnverifiedEmailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
