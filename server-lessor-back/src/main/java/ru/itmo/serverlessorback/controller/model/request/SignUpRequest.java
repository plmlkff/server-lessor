package ru.itmo.serverlessorback.controller.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotNull
    private String login;

    @NotNull
    private String password;

    private Integer refCode;
}
