package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import ru.itmo.serverlessorback.controller.model.response.AuthUserResponse;

public interface AuthService {
    Either<Exception, AuthUserResponse> signUp(String login, String password, Integer refCode);

    Either<Exception, AuthUserResponse> login(String login, String password);
}
