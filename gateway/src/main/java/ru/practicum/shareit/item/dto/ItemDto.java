package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private long id;

    @NotBlank
    @Size(max = 100, message = "максимальная длина описания - 100 символов")
    private String name;

    @Size(max = 500, message = "максимальная длина описания - 500 символов")
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private Long request;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    private Long requestId;
}
