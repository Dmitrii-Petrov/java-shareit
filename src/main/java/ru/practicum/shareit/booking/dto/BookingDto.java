package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    @NotNull
    private Long itemId;

    private Long booker;

    @Enumerated(EnumType.STRING)
    private Status status;
}
