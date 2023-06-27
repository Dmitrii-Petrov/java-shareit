package ru.practicum.shareit.user.storage;

public class EmailAlreadyTakenException extends IllegalArgumentException {

    public EmailAlreadyTakenException() {
        super("Пользователь с таким email уже есть");
    }
}
