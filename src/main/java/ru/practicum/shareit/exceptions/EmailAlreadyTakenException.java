package ru.practicum.shareit.exceptions;

public class EmailAlreadyTakenException extends IllegalArgumentException {

    public EmailAlreadyTakenException() {
        super("Пользователь с таким email уже есть");
    }
}
