package ru.clevertec.webservlet.exception;

public class SessionInvalidException extends RuntimeException {

    public SessionInvalidException(String message) {
        super(message);
    }

}
