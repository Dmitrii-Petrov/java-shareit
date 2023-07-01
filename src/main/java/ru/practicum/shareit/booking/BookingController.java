package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @PatchMapping("/{bookingId}")
    public Booking update(@PathVariable @NotNull Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Boolean approved) {
        log.debug("поулчен запрос PATCH /bookings");
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable @NotNull Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /bookings");
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("поулчен запрос GET /bookings");
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("поулчен запрос GET /bookings/owner");
        return bookingService.getBookingsByOwner(userId, state);
    }
}
