package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryBookingStorage implements BookingStorage {
    HashMap<Long, Booking> bookingRepository = new HashMap<>();
    long generatorId = 0;

    public long generateId() {
        return ++generatorId;
    }

    @Override
    public void save(Booking booking) {
        booking.setId(generateId());
        bookingRepository.put(booking.getId(), booking);
    }

    @Override
    public void update(Booking booking) {
        if (!bookingRepository.containsKey(booking.getId())) {
            throw new NotFoundEntityException();
        }
        bookingRepository.put(booking.getId(), booking);
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        bookingRepository.remove(id);
    }


    @Override
    public Booking getBooking(Long id) {
        if (!bookingRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        return bookingRepository.get(id);
    }

    @Override
    public ArrayList<Booking> getBookings() {
        return new ArrayList<>(bookingRepository.values());
    }


}
