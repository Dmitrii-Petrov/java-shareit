package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
@AllArgsConstructor
public class BookingMapper {
//    public static BookingDto bookingToDto(Booking booking) {
//        return new BookingDto(
//                booking.getId(),
//                booking.getStart(),
//                booking.getEnd(),
//                booking.getItem().getId(),
//                booking.getUser(),
//                booking.getStatus()
//        );
//    }

//    public static Booking dtoToBooking(Long id, BookingDto bookingDto) {
//        return new Booking(
//                id,
//                userDto.getName(),
//                userDto.getEmail()
//        );
//    }

    public static Booking mapToNewBooking(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        if (bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            booking.setEnd(bookingDto.getEnd());
        } else throw new WrongEntityException();
        booking.setItem(item);
//        booking.setBookerId(booker.getId());
//        booking.setItemId(item.getId());
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        return booking;
    }

}
