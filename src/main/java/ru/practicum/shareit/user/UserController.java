package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    public User getUsersById(@PathVariable(required = false) @NotNull Long userId) {
        log.info("поулчен запрос GET /users/id");
        return userService.getUsersById(userId);

    }


    @PostMapping()
    public User create(@RequestBody @Valid User user) {
        log.info("поулчен запрос POST /users");
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable @NotNull Long userId, @RequestBody UserDto userDto) {
        log.debug("поулчен запрос PATCH /users");
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable @NotNull Long userId) {
        log.debug("поулчен запрос DELETE /users");
        userService.delete(userId);
    }


}
