package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.WrongEntityException;

@Component
@AllArgsConstructor
public class BookingMapper {
    public static BookingDto bookingToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

//    public static Booking dtoToBooking(Long id, BookingDto bookingDto) {
//        return new Booking(
//                id,
//                userDto.getName(),
//                userDto.getEmail()
//        );
//    }

    public static Booking mapToNewBooking(BookingDto bookingDto, Long id) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        if (bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            booking.setEnd(bookingDto.getEnd());
        } else throw new WrongEntityException();
        booking.setItem(bookingDto.getItemId());
        booking.setBooker(id);
        booking.setStatus(Status.WAITING);
        return booking;
    }

}
