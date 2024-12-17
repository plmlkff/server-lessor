package ru.itmo.serverlessorback.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponse {
    public static ResponseEntity<Void> ok() {
        return ResponseEntity.ok().build();
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok()
                .body(body);
    }

    public static ResponseEntity<String> error() {
        return error(HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> error(int code) {
        return error(HttpStatus.valueOf(code));
    }

    public static ResponseEntity<String> error(HttpStatus status) {
        return ResponseEntity.status(status)
                .body(status.getReasonPhrase());
    }

    public static ResponseEntity<String> error(String reason) {
        return error(HttpStatus.BAD_REQUEST, reason);
    }

    public static ResponseEntity<String> error(HttpStatus status, String reason) {
        return ResponseEntity.status(status)
                .body(reason);
    }
}
