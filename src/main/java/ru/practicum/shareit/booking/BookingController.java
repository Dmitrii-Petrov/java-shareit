package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public Booking create(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос POST /bookings");
        return bookingService.saveBooking(bookingDto, userId);
    }
}
