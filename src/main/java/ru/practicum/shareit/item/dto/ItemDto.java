package ru.practicum.shareit.item.dto;

import lombok.Data;

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


    private Long owner;


    private Long request;

//    public ItemDto(String name, String description, Boolean available, Long request) {
//        this.name = name;
//        this.description = description;
//        this.available = available;
//        this.request = request;
//    }


}
