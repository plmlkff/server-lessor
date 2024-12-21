package ru.itmo.serverlessorback.exception;

public class CanNotCreateConfigurationException extends RuntimeException{
    public CanNotCreateConfigurationException(String message) {
        super(message);
    }

    public CanNotCreateConfigurationException() {
    }
}
