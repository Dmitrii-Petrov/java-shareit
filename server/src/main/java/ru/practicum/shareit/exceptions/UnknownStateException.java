package ru.practicum.shareit.exceptions;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String s) {
        super(s);
    }
}
