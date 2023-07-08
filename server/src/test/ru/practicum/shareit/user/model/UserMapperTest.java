package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.user.model.UserMapper.mapToNewUser;
import static ru.practicum.shareit.user.model.UserMapper.userToDto;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @InjectMocks
    private UserMapper userMapper;


    @Test
    void userToDtoTest() {
        long userId = 0L;
        User user = new User(userId, "name", "mail@mail.com");
        UserDto userDto = userToDto(user);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void mapToNewUserTest() {
        long userId = 0L;
        UserDto userDto = new UserDto(userId, "name", "mail@mail.com");
        User user = mapToNewUser(userDto);

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}