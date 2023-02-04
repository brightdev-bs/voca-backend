package vanille.vocabe.global.exception;

import lombok.Getter;
import vanille.vocabe.global.constants.ErrorCode;

@Getter
public class InvalidHeaderException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidHeaderException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
