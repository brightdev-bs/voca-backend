package vanille.vocabe.global.exception;

import lombok.Getter;
import vanille.vocabe.global.constants.ErrorCode;

@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
