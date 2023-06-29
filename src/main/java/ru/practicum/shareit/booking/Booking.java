package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@Builder
public class Booking {

    private Long id;

    @NotBlank
    private String name;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long item;

    private Long booker;

    private Status status;
}
