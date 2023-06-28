package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class ItemDto {

    private long id;

    @NotBlank
    private String name;

    @Size(max = 200, message = "максимальная длина описания - 200 символов")
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;


    private User owner;

    private ItemRequest request;

    public ItemDto(String name, String description, Boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }


}
