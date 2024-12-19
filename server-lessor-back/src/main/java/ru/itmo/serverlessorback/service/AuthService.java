package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import ru.itmo.serverlessorback.controller.model.response.JwtResponse;

public interface AuthService {
    Either<Exception, JwtResponse> signUp(String login, String password, Integer refCode);

    Either<Exception, JwtResponse> login(String login, String password);
}
