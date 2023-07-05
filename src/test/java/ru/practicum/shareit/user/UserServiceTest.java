package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.user.model.UserMapper.userToDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void getUsers() {
        User user = new User();

        ArrayList<User> list = new ArrayList<>();
        list.add(user);

        when(userRepository.findAll()).thenReturn(list);

        List<User> response = userService.getUsers();

        assertEquals(list.get(0), response.get(0));
    }

    @Test
    void findUserById_whenUserFound_thenReturnTrue() {
        long userId = 0L;

        when(userRepository.existsById(userId)).thenReturn(true);

        assertTrue(userService.findUserById(userId));
    }

    @Test
    void findUserById_whenUserNotFound_thenReturnFalse() {
        long userId = 0L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertFalse(userService.findUserById(userId));
    }

    @Test
    void getUsersById_whenUserFound_thenReturnUser() {
        long userId = 0L;
        User user = new User(userId, "name", "mail@mail.com");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> response = userService.getUsersById(userId);

        assertEquals(Optional.of(user), response);
    }

    @Test
    void getUsersById_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> userService.getUsersById(userId));

        verify(userRepository, never()).findById(userId);
    }

    @Test
    void saveUser() {
        User user = new User(null, "name", "mail@mail.com");
        UserDto userDto = userToDto(user);

        when(userRepository.save(user)).thenReturn(user);

        User response = userService.saveUser(userDto);

        assertEquals(user, response);
        verify(userRepository).save(user);

    }

    @Test
    void updateUser_whenUserFound_thenReturnUpdatedUser() {
        long userId = 0L;
        User user = new User(userId, "name", "mail@mail.com");
        UserDto userDto = userToDto(user);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User response = userService.updateUser(userId, userDto);

        assertEquals(user, response);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_whenUserDtoNameAndEmailNulls_thenReturnUpdatedUser() {
        long userId = 0L;
        User user = new User(userId, "name", "mail@mail.com");
        UserDto userDto = userToDto(user);
        userDto.setName(null);
        userDto.setEmail(null);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User response = userService.updateUser(userId, userDto);

        assertEquals(user, response);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        User user = new User(null, "name", "mail@mail.com");
        UserDto userDto = userToDto(user);

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> userService.updateUser(userId,userDto));

        verify(userRepository, never()).findById(userId);
    }


    @Test
    void delete() {
        long userId = 0L;
        User user = new User(userId, "name", "mail@mail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);

    }
}