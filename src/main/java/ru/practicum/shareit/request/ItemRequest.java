package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {

    private Long id;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    private String description;

    private User requestor;

    private LocalDateTime created;
}
