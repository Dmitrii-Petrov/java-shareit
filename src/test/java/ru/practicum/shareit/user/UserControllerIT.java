package ru.practicum.shareit.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.model.UserMapper.mapToNewUser;

@WebMvcTest(controllers = UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void getUsers() {
        mvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getUsers();
    }

    @SneakyThrows
    @Test
    void getUsersById() {
        long userId = 0L;

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).getUsersById(userId);

    }

    @SneakyThrows
    @Test
    void create_whenUserDtoIsValid_thenReturnedOk() {

        UserDto userDto = new UserDto(0L, "name", "mail@mail.com");
        User user = mapToNewUser(userDto);
        when(userService.saveUser(userDto)).thenReturn(user);

        String result = mvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).saveUser(userDto);
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @SneakyThrows
    @Test
    void create_whenUserDtoIsNotValid_thenReturnedBadRequest() {

        UserDto userDto = new UserDto(0L, "name", "mail@mail.com");

        userDto.setName(null);
        User user = mapToNewUser(userDto);
        when(userService.saveUser(userDto)).thenReturn(user);

        mvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).saveUser(userDto);
    }


    @SneakyThrows
    @Test
    void update_whenUserDtoIsValid_thenReturnedOk() {

        UserDto userDto = new UserDto(0L, "name", "mail@mail.com");
        User user = mapToNewUser(userDto);
        when(userService.updateUser(userDto.getId(), userDto)).thenReturn(user);

        String result = mvc.perform(patch("/users/{userId}", userDto.getId(), userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).updateUser(userDto.getId(), userDto);
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

}