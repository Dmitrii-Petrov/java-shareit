package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.ItemService;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.BookingMapper.mapToNewBooking;

@Service("bookingService")
@RequiredArgsConstructor
public class BookingService {

    BookingRepository bookingRepository;

    ItemService itemService;


    @Autowired
    public BookingService(BookingRepository bookingRepository, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
    }

//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

//    public UserStorage getUserStorage() {
//        return userStorage;
//    }

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
        Booking booking = mapToNewBooking(bookingDto, id);
        if (!itemService.findUserById(id)) {
            throw new NotFoundEntityException();
        }
        if (!itemService.getItemById(booking.getItem()).get().getAvailable()) {
            throw new WrongEntityException();
        }
        bookingRepository.save(booking);
        return booking;
    }

//    public User updateUser(Long id, UserDto userDto) {
//        if (!userRepository.existsById(id)) {
//            throw new NotFoundEntityException();
//        }
//        User user = userRepository.findById(id).get();
//        return userRepository.save(new User(
//                id,
//                userDto.getName() != null ? userDto.getName() : user.getName(),
//                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail()
//        ));
//    }

//    public void delete(Long userId) {
//        userRepository.delete(userRepository.findById(userId).get());
//    }
}
