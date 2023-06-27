package ru.practicum.shareit.user.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException() {
        super("Такого пользователя не существует");
    }
}
