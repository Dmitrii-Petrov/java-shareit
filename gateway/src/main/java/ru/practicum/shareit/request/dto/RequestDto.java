package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestDto {

    private Long id;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    @NotBlank
    private String description;

    private UserDto requestor;

    private LocalDateTime created;

    private List<ItemDto> items;
}
