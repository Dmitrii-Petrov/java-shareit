package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;

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
    private String name;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private Long request;

    private Booking lastBooking;

    private Booking nextBooking;

    private List<CommentDto> comments = new ArrayList<>();
}
