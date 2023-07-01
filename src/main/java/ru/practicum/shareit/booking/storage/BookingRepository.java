package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByIdDesc(Long bookerId);

    List<Booking> findByItemOwnerOrderByIdDesc(Long owner);

    List<Booking> findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc(Long owner, LocalDateTime end, LocalDateTime start);

    List<Booking> findByItemOwnerAndEndBeforeOrderByStartDesc(Long owner, LocalDateTime end);

    List<Booking> findByItemOwnerAndStartAfterOrderByStartDesc(Long owner, LocalDateTime end);

    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long owner, Status status);

    List<Booking> findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(Long booker, LocalDateTime end, LocalDateTime start);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long booker, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long booker, LocalDateTime end);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long booker, Status status);

    List<Booking> findByItemIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);

    List<Booking> findByItemIdAndEndBeforeOrderByEndAsc(Long id, LocalDateTime end);

}

