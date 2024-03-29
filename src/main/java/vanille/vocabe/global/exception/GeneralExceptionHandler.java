package vanille.vocabe.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vanille.vocabe.global.response.common.ApiResponse;

import javax.mail.AuthenticationFailedException;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     */
    @ExceptionHandler({BindException.class})
    protected ResponseEntity<ApiResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.METHOD_NOT_ALLOWED.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(value = { BusinessException.class })
    protected ResponseEntity<ApiResponse> handleConflict(BusinessException e) {
        log.error("BusinessException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 엔티티가 존재하지 않을 때 오류 발생
     */
    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * header 정보가 부적절할 때 오류 발생
     */
    @ExceptionHandler(value = { InvalidHeaderException.class })
    protected ResponseEntity<ApiResponse> handleNotFoundException(InvalidHeaderException e) {
        log.error("InvalidHeaderException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 중복 데이터를 넣을려고 할 때 오류 발생
     */
    @ExceptionHandler(value = { DuplicatedEntityException.class })
    protected ResponseEntity<ApiResponse> handleNotFoundException(DuplicatedEntityException e) {
        log.error("DuplicatedEntityException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 이메일 인증 안한 경우
     */
    @ExceptionHandler(value = { UnverifiedException.class })
    protected ResponseEntity<ApiResponse> handleUnverifiedException(UnverifiedException e) {
        log.error("UnverifiedEmailException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 이메일 인증 코드가 부적절한 경우
     */
    @ExceptionHandler(value = { InvalidVerificationCodeException.class })
    protected ResponseEntity<ApiResponse> handleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
        log.error("InvalidPasswordException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 패스워드가 일치하지 않을 때
     */
    @ExceptionHandler(value = { InvalidPasswordException.class })
    protected ResponseEntity<ApiResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        log.error("InvalidPasswordException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 토큰이 유효하지 않을 때
     */
    @ExceptionHandler(value = { ExpiredTokenException.class })
    protected ResponseEntity<ApiResponse> handleInvalidTokenException(ExpiredTokenException e) {
        log.error("InvalidPasswordException", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = {AuthenticationException.class} )
    protected ResponseEntity<ApiResponse> authenticationFailedExpcetion(Exception e) {
        log.error("AuthenticationFailed", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 나머지 예외 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse> handleException(Exception e) {
        log.error("Exception", e);
        ApiResponse errorResponse = ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
