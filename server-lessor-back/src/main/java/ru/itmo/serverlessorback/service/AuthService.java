package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import ru.itmo.serverlessorback.controller.model.response.UserResponse;

public interface AuthService {
    Either<Exception, UserResponse> signUp(String login, String password, Integer refCode);

    Either<Exception, UserResponse> login(String login, String password);
}
