package ru.practicum.shareit.request.storage;

public class ItemRequestNotFoundException extends IllegalArgumentException {
    public ItemRequestNotFoundException() {
        super("Такого запроса не существует");
    }
}
