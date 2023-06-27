package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class ItemRequestDto {
    private Long id;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    private String description;

    private User requestor;

    private LocalDateTime created;
}
