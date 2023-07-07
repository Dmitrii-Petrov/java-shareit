package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerIdOrderByIdDesc(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStatusAndEndBeforeOrderByIdDesc(Long bookerId, Status status, LocalDateTime time);

    Page<Booking> findByItemOwnerOrderByIdDesc(Long owner, Pageable pageable);

    Page<Booking> findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc(Long owner, LocalDateTime end, LocalDateTime start, Pageable pageable);

    Page<Booking> findByItemOwnerAndEndBeforeOrderByStartDesc(Long owner, LocalDateTime end, Pageable pageable);

    Page<Booking> findByItemOwnerAndStartAfterOrderByStartDesc(Long owner, LocalDateTime end, Pageable pageable);

    Page<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long owner, Status status, Pageable pageable);

    Page<Booking> findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(Long booker, LocalDateTime end, LocalDateTime start, Pageable pageable);

    Page<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long booker, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long booker, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long booker, Status status, Pageable pageable);

    List<Booking> findByItemIdAndStartAfterAndStatusOrderByStartAsc(Long id, LocalDateTime start, Status status);

    List<Booking> findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long id, LocalDateTime end, Status status);

}

