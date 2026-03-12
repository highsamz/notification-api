package br.com.avaliacao_globo.infrastructure.web.controller;

import br.com.avaliacao_globo.exception.MessagePublishException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Validation failed");
        response.put("fieldErrors", fieldErrors);

        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMalformedJson(HttpMessageNotReadableException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", "Malformed or invalid request body"
        );
    }

    @ExceptionHandler(MessagePublishException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> handlePublishError(MessagePublishException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Service Unavailable",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUnexpected(Exception ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", "Unexpected internal error"
        );
    }
}