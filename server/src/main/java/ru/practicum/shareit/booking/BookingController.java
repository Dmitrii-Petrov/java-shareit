package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

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
    public Booking create(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос POST /bookings");
        return bookingService.saveBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking update(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(name = "approved") Boolean approved) {
        log.debug("поулчен запрос PATCH /bookings");
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /bookings");
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(required = false, defaultValue = "ALL") String state,
                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @RequestParam(required = false) Integer size) {
        log.info("поулчен запрос GET /bookings");
        Optional<BookingState> stateParam = BookingState.from(state);
        return bookingService.getBookings(userId, stateParam.get(), from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false, defaultValue = "ALL") String state,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false) Integer size) {
        log.info("поулчен запрос GET /bookings/owner");
        Optional<BookingState> stateParam = BookingState.from(state);
        return bookingService.getBookingsByOwner(userId, stateParam.get(), from, size);
    }
}
