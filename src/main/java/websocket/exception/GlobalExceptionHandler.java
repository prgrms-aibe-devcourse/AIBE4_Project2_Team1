package websocket.exception;

import websocket.dto.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException; // 추가됨
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException; // 추가됨
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // 로깅 기능 활성화
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 📌 [통합처리] 의도적인 비즈니스 예외(400 Bad Request) - 비즈니스 로직 위반(중복 예약 등)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponse<Void>> handleCommonException(RuntimeException e) {
        log.warn("Business Exception: {}", e.getMessage()); // 의도된 예외이므로 warn 레벨로 로그 기록
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
    }

    // 📌 @Valid 검증 실패(400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Failed: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage));
    }

    // 📌 파라미터 타입 불일치(400 Bad Request)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("'%s' 값의 형식이 올바르지 않습니다. (입력값: %s)", e.getName(), e.getValue());
        log.warn("Type Mismatch: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage));
    }

    // 📌 필수 파라미터 누락(400 Bad Request)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameterException(MissingServletRequestParameterException e) {
        String errorMessage = String.format("필수 파라미터 '%s'가 누락되었습니다.", e.getParameterName());
        log.warn("Missing Parameter: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage));
    }

    // 📌 JSON 파싱 오류(400 Bad Request)
    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleJsonException(HttpMessageNotReadableException e) {
        log.warn("Json Parse Error: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("요청한 JSON 본문의 형식이 올바르지 않습니다."));
    }
    // 📌 잘못된 HTTP Method 요청(405 Method Not Allowed)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handlerMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("Http Method Not Supported: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error("지원하지 않는 HTTP 메서드입니다."));
    }

    // 📌권한 부족 예외(403 Forbidden)
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<Void>> handlerSecurityException(SecurityException e) {
        log.warn("Security Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(e.getMessage()));
    }

    // 📌 커스텀 예외(400 Bad Request)
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateException(InvalidDateException e){
        log.warn("Invalid Date: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
    }

    // 📌 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}