package ru.practicum.shareit.booking.storage;

public class BookingNotFoundException extends IllegalArgumentException {
    public BookingNotFoundException() {
        super("Такого бронирования не существует");
    }
}
