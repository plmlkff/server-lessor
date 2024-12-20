package ru.itmo.serverlessorback.utils;

import lombok.Data;

@Data
public class ProtocolCredentials{
    private final String username;

    private final String password;

    private final String host;

    private final int port;
}
