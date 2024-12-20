package ru.itmo.serverlessorback.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorResponse> handleThrowableException(Throwable ex, WebRequest request) {
        logger.error("Unexpected error", ex);
        String message = (ex.getMessage() == null) ?
                "Unexpected error" : "Unexpected error: %s".formatted(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(MissingRequestValueException.class)
    ResponseEntity<ErrorResponse> handleMissingValue(MissingRequestValueException ex, WebRequest request) {
        String message = (ex.getMessage() == null) ?
                "Error" : "Error: %s".formatted(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message));
    }
}
