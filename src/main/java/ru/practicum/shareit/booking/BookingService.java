package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.model.BookingMapper.mapToNewBooking;

@Service("bookingService")
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

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
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<Booking> getBookings(Long userId, String state, Integer from, Integer size) {
        if (!itemService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage;

        switch (state) {
            case "ALL": {
                bookingPage = bookingRepository.findByBookerIdOrderByIdDesc(userId, page);
                break;
            }
            case "CURRENT": {
                bookingPage = bookingRepository.findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            }
            case "PAST": {
                bookingPage = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), page);
                break;
            }
            case "FUTURE": {
                bookingPage = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), page);
                break;
            }
            case "WAITING": {
                bookingPage = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            }
            case "REJECTED": {
                bookingPage = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
            }
            default:
                throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        }


        List<Booking> result = new ArrayList<>(bookingPage.getContent());
        if (result.size() > from % size) {
            result = result.subList(from % size, result.size());
        } else result.clear();

        return result;
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByOwner(Long userId, String state, Integer from, Integer size) {
        if (!itemService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage;

        switch (state) {
            case "ALL": {
                bookingPage = bookingRepository.findByItemOwnerOrderByIdDesc(userId, page);
                break;
            }
            case "CURRENT": {
                bookingPage = bookingRepository.findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            }
            case "PAST": {
                bookingPage = bookingRepository.findByItemOwnerAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), page);
                break;
            }
            case "FUTURE": {
                bookingPage = bookingRepository.findByItemOwnerAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), page);
                break;
            }
            case "WAITING": {
                bookingPage = bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            }
            case "REJECTED": {
                bookingPage = bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
            }
            default:
                throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        }

        List<Booking> result = new ArrayList<>(bookingPage.getContent());
        if (result.size() > from % size) {
            result = result.subList(from % size, result.size());
        } else result.clear();


        return result;
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
