package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongEntityException extends RuntimeException {
    public WrongEntityException(String s) {
        super(s);
    }

    public WrongEntityException() {
    }
}
