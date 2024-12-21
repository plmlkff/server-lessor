package ru.itmo.serverlessorback.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorResponse> handleThrowableException(Throwable ex, WebRequest request) {
        logger.error("Неожиданная ошибка", ex);
        String message = (ex.getMessage() == null) ?
                "Неожиданная ошибка" : "Неожиданная ошибка: %s".formatted(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = (ex.getMessage() == null) ?
                "Ошибка" : "Ошибка: %s".formatted(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Невалидные данные в запросе"));
    }

    @ExceptionHandler(MissingRequestValueException.class)
    ResponseEntity<ErrorResponse> handleMissingValue(MissingRequestValueException ex, WebRequest request) {
        String message = (ex.getMessage() == null) ?
                "Ошибка" : "Ошибка: %s".formatted(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "Ошибка: %s".formatted(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message));
    }
}
