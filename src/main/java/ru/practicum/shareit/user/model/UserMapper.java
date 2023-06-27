package ru.practicum.shareit.user.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper {
    public static UserDto userToDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

    public static User dtoToUser(Long id, UserDto userDto) {
        return new User(
                id,
                userDto.getName() != null ? userDto.getName() : null,
                userDto.getEmail() != null ? userDto.getEmail() : null
        );
    }


}
