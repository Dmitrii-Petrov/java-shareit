package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {
    private Long id;
    private String name;

    @Email
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
