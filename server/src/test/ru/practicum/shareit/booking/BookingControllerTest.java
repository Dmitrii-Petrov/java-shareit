package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;


    @Test
    void getBookings() {
        long userId = 0L;
        Integer from = 0;
        Integer size = 10;
        String state = "ALL";
        Booking booking = new Booking();

        Optional<BookingState> stateParam = BookingState.from(state);

        when(bookingService.getBookings(userId, stateParam.get(), from, size)).thenReturn(List.of(booking));

        List<Booking> result = bookingController.getBookings(userId, state, from, size);

        assertEquals(booking, result.get(0));
    }

    @Test
    void getBookingsByOwner() {
        long userId = 0L;
        Integer from = 0;
        Integer size = 10;
        String state = "ALL";
        Booking booking = new Booking();

        Optional<BookingState> stateParam = BookingState.from(state);

        when(bookingService.getBookings(userId, stateParam.get(), from, size)).thenReturn(List.of(booking));

        List<Booking> result = bookingController.getBookings(userId, state, from, size);

        assertEquals(booking, result.get(0));
    }
}