package ru.itmo.serverlessorback.exception;

public class UnknownProtocolException extends Exception{
    public UnknownProtocolException() {
    }

    public UnknownProtocolException(String message) {
        super(message);
    }
}
