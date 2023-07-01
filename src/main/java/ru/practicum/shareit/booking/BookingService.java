package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.BookingMapper.mapToNewBooking;

@Service("bookingService")
@RequiredArgsConstructor
public class BookingService {

    BookingRepository bookingRepository;
    ItemService itemService;
    UserService userService;


    @Autowired
    public BookingService(BookingRepository bookingRepository, ItemService itemService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
        this.userService = userService;
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Boolean findBookingById(Long bookingId) {
        return bookingRepository.existsById(bookingId);
    }

    public Optional<Booking> getBookingsById(Long bookingsId) {
        if (!bookingRepository.existsById(bookingsId)) {
            throw new NotFoundEntityException();
        }
        return bookingRepository.findById(bookingsId);
    }

    public Booking saveBooking(BookingDto bookingDto, Long id) {
        Item item = itemService.getItemById(bookingDto.getItemId());
        User user = userService.getUsersById(id).get();
        Booking booking = mapToNewBooking(bookingDto, user, item);

        if (!itemService.findUserById(id)) {
            throw new NotFoundEntityException();
        }
        if (!item.getAvailable()) {
            throw new WrongEntityException();
        }
        if (item.getOwner().equals(id)) {
            throw new NotFoundEntityException();
        }
        bookingRepository.save(booking);
        return booking;
    }

    public Booking getBookingById(Long bookingId, Long userId) {
        if ((!bookingRepository.existsById(bookingId)) || (!itemService.findUserById(userId))) {
            throw new NotFoundEntityException();
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!((Objects.equals(booking.getBooker().getId(), userId)) || (Objects.equals(booking.getItem().getOwner(), userId)))) {
            throw new NotFoundEntityException();
        }
        return booking;
    }

    public List<Booking> getBookings(Long userId, String state) {
        if (!itemService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        switch (state) {
            case "ALL": {
                return bookingRepository.findByBookerIdOrderByIdDesc(userId);
            }
            case "CURRENT": {
                return bookingRepository.findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            }
            case "PAST": {
                return bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "FUTURE": {
                return bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "WAITING": {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            }
            case "REJECTED": {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            }
            default:
                throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<Booking> getBookingsByOwner(Long userId, String state) {
        if (!itemService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        switch (state) {
            case "ALL": {
                return bookingRepository.findByItemOwnerOrderByIdDesc(userId);
            }
            case "CURRENT": {
                return bookingRepository.findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            }
            case "PAST": {
                return bookingRepository.findByItemOwnerAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "FUTURE": {
                return bookingRepository.findByItemOwnerAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "WAITING": {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.WAITING);
            }
            case "REJECTED": {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.REJECTED);
            }
            default:
                throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public Booking approve(Long bookingId, Long userId, Boolean bool) {
        if ((!userService.findUserById(userId)) || (!bookingRepository.existsById(bookingId)) || (!Objects.equals(bookingRepository.findById(bookingId).get().getItem().getOwner(), userId))) {
            throw new NotFoundEntityException();
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new WrongEntityException();
        }
        if (bool) {
            booking.setStatus(Status.APPROVED);
        } else booking.setStatus(Status.REJECTED);
        return bookingRepository.save(booking);
    }
}
