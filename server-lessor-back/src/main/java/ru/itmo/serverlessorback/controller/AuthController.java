package ru.itmo.serverlessorback.controller;

import io.vavr.control.Either;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.serverlessorback.controller.model.request.LoginRequest;
import ru.itmo.serverlessorback.controller.model.request.SignUpRequest;
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse;
import ru.itmo.serverlessorback.exception.AlreadyExistsException;
import ru.itmo.serverlessorback.exception.AuthenticationException;
import ru.itmo.serverlessorback.exception.NotFoundException;
import ru.itmo.serverlessorback.service.AuthService;
import ru.itmo.serverlessorback.utils.HttpResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return toResponse(
                authService.signUp(
                        signUpRequest.getLogin(),
                        signUpRequest.getPassword(),
                        signUpRequest.getRefCode()
                )
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return toResponse(
                authService.login(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
    }

    public ResponseEntity<?> toResponse(Either<? extends Exception, ?> either) {
        return either.fold(
                error -> {
                    if (error instanceof NotFoundException) {
                        return HttpResponse.notFound(new ErrorResponse(error.getMessage()));
                    }
                    else if (error instanceof AlreadyExistsException) {
                        return HttpResponse.conflict(new ErrorResponse(error.getMessage()));
                    }
                    else if (error instanceof AuthenticationException) {
                        return HttpResponse.unauthorized(new ErrorResponse(error.getMessage()));
                    }
                    else {
                        return HttpResponse.unexpectedError(
                                new ErrorResponse("Неожиданная ошибка: %s".formatted(error.getMessage()))
                        );
                    }
                },
                HttpResponse::ok
        );
    }
}
