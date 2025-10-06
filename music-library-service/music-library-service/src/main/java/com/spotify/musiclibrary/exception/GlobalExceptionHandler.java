package com.spotify.musiclibrary.exception;

import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Feign exception");
        error.put("message", ex.getMessage());
        error.put("status", ex.status());
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.status()));
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Map<String, Object>> handleRetryableException(RetryableException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Service unavailable");
        error.put("message", ex.getMessage());
        error.put("cause", ex.getCause() != null ? ex.getCause().getMessage() : null);
        error.put("status", 503);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({SocketTimeoutException.class, ConnectException.class})
    public ResponseEntity<Map<String, Object>> handleNetworkException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Network error");
        error.put("message", ex.getMessage());
        error.put("status", 504);
        return new ResponseEntity<>(error, HttpStatus.GATEWAY_TIMEOUT);
    }
}
