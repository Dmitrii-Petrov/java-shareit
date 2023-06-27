package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
public class Item {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private ItemRequest request;


}
