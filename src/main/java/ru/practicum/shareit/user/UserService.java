package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static ru.practicum.shareit.user.model.UserMapper.dtoToUser;

@Service("userService")
@Data
public class UserService {

    UserStorage userStorage;


    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public List<User> getUsers() {
        return getUserStorage().getUsers();
    }

    public User getUsersById(Long userId) {
        return getUserStorage().getUser(userId);
    }

    public User create(User user) {
        getUserStorage().save(user);
        return user;
    }

    public User update(Long id, UserDto userDto) {
        User user = dtoToUser(id, userDto);
        getUserStorage().update(id, user);
        return getUserStorage().getUser(id);
    }

    public void delete(Long id) {
        getUserStorage().delete(id);
    }

}
