package org.example.kiosk_manage.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.kiosk_manage.common.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("[Validation] {}", message);
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
        log.warn("[BadRequest] {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> unAuthorizedException(UnAuthorizedException e) {
        log.warn("[Unauthorized] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        log.warn("[NotFound] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflictException(ConflictException e) {
        log.warn("[Conflict] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // 읽을 수 없는 본문, 형식이 다른 파라미터
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleUnreadable(Exception e) {
        log.warn("[BadRequest] 요청 형식 오류: {}", e.getClass().getSimpleName());
        return ResponseEntity.badRequest().body(new ErrorResponse("요청 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> runtimeException(Exception e, HttpServletRequest request) {
        // 없는 주소(404), 잘못된 메서드(405), 필수 파라미터 누락(400) 등 스프링이 상태를 정해 둔 잘못된 요청
        if (e instanceof org.springframework.web.ErrorResponse spring
                && spring.getStatusCode().is4xxClientError()) {
            int status = spring.getStatusCode().value();
            log.warn("[{}] {} {}", status, request.getMethod(), singleLine(request.getRequestURI()));
            return ResponseEntity.status(status).body(new ErrorResponse(clientMessage(status)));
        }
        // 내부 오류의 내용은 로그에만 남기고 응답에는 담지 않는다
        log.error("[UnhandledException] {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 오류가 발생했습니다."));
    }

    private static String clientMessage(int status) {
        return switch (status) {
            case 404 -> "존재하지 않는 주소입니다.";
            case 405 -> "지원하지 않는 요청 방식입니다.";
            case 415 -> "지원하지 않는 데이터 형식입니다.";
            default -> "잘못된 요청입니다.";
        };
    }

    // 줄바꿈을 제거해 로그 위조를 막는다
    private static String singleLine(String value) {
        return value == null ? "-" : value.replaceAll("[\\r\\n]", "_");
    }

}
