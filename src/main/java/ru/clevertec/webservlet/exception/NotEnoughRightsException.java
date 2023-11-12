package ru.clevertec.webservlet.exception;

public class NotEnoughRightsException extends RuntimeException {

    public NotEnoughRightsException(String message) {
        super(message);
    }

}
