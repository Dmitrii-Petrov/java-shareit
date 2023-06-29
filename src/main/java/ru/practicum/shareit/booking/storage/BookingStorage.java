package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;

public interface BookingStorage {
    void save(Booking booking);

    void update(Booking booking);

    void delete(Long id);

    Booking getBooking(Long id);

    ArrayList<Booking> getBookings();
}
