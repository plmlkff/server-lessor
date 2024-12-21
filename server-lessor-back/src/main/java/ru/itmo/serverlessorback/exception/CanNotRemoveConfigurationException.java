package ru.itmo.serverlessorback.exception;

public class CanNotRemoveConfigurationException extends RuntimeException{
    public CanNotRemoveConfigurationException() {
    }

    public CanNotRemoveConfigurationException(String message) {
        super(message);
    }
}
