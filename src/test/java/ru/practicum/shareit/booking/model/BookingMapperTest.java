package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.model.BookingMapper.mapToNewBooking;
import static ru.practicum.shareit.request.model.RequestMapper.mapToNewRequest;

class BookingMapperTest {

    @Test
    void mapToNewBookingTest() {
        User user = new User();
        long bookerId = 0L;
        user.setId(bookerId);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = time.plusDays(1);
        Item item = new Item();
        long itemId = 0L;
        item.setId(itemId);

        BookingDto bookingDto = new BookingDto(0L, time,time2, itemId,bookerId, item, user, Status.WAITING);

        Booking booking = mapToNewBooking(bookingDto, user, item);

        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getBooker(), user);
    }

    @Test
    void mapToNewBookingTest_whenStartAfterEnd_thenWrongEntityExceptionThrown () {
        User user = new User();
        long bookerId = 0L;
        user.setId(bookerId);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = time.minusDays(1);
        Item item = new Item();
        long itemId = 0L;
        item.setId(itemId);

        BookingDto bookingDto = new BookingDto(0L, time,time2, itemId,bookerId, item, user, Status.WAITING);

        assertThrows(WrongEntityException.class,
                () -> mapToNewBooking(bookingDto, user, item));
    }
}