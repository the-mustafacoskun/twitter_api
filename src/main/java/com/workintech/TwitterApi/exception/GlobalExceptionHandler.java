package com.workintech.TwitterApi.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleException(ApiException apiException) {
        ErrorResponse errorResponse = new ErrorResponse(apiException.getHttpStatus().value(),apiException.getMessage(),java.time.LocalDateTime.now());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse,apiException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(),java.time.LocalDateTime.now());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
