package ru.itmo.serverlessorback.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse;

public class HttpResponse {
//    public static ResponseEntity<Void> ok() {
//        return ResponseEntity.ok().build();
//    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ok().body(body);
    }

    public static ResponseEntity<ErrorResponse> badRequest(ErrorResponse body) {
        return error(HttpStatus.BAD_REQUEST, body);
    }

    public static ResponseEntity<ErrorResponse> unexpectedError(ErrorResponse body) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, body);
    }

    public static ResponseEntity<ErrorResponse> notFound(ErrorResponse body) {
        return error(HttpStatus.NOT_FOUND, body);
    }

    public static ResponseEntity<ErrorResponse> error(HttpStatus status, ErrorResponse body) {
        return ResponseEntity.status(status)
                .body(body);
    }
}
