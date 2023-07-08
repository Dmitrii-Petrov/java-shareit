package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getUsers() {
        log.info("поулчен запрос GET /users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public Optional<User> getUsersById(@PathVariable(required = false) Long userId) {
        log.info("поулчен запрос GET /users/id");
        return userService.getUsersById(userId);
    }


    @PostMapping()
    public User create(@RequestBody UserDto userDto) {
        log.info("поулчен запрос POST /users");
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.debug("поулчен запрос PATCH /users");
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.debug("поулчен запрос DELETE /users");
        userService.delete(userId);
    }
}
