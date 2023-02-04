package vanille.vocabe.global.exception;

import lombok.Getter;
import vanille.vocabe.global.constants.ErrorCode;

@Getter
public class DuplicatedEntityException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicatedEntityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
