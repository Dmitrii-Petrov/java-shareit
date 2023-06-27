package ru.practicum.shareit.item.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends IllegalArgumentException {
    public ItemNotFoundException() {
        super("Такого предмета не существует");
    }
}
