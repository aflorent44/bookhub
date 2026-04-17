package fr.bookhub.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ServiceResponse<?>> handleApiException(ApiException ex) {

        ApiCode code = ex.getApiCode();
        HttpStatus status = code.httpStatus();

        return ResponseEntity
                .status(status)
                .body(new ServiceResponse<>(code));
    }
}