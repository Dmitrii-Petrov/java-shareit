package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.Booking;

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
            throw new BookingNotFoundException();
        }
        bookingRepository.put(booking.getId(), booking);
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepository.containsKey(id)) {
            throw new BookingNotFoundException();
        }
        bookingRepository.remove(id);
    }


    @Override
    public Booking getBooking(Long id) {
        if (!bookingRepository.containsKey(id)) {
            throw new BookingNotFoundException();
        }
        return bookingRepository.get(id);
    }

    @Override
    public ArrayList<Booking> getBookings() {
        return new ArrayList<>(bookingRepository.values());
    }


}
