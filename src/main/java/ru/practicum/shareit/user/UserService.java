package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.model.UserMapper.mapToNewUser;

@Service("userService")
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Boolean findUserById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUsersById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException();
        }
        return userRepository.findById(userId);
    }

    public User saveUser(UserDto userDto) {
        User user = mapToNewUser(userDto);
        userRepository.save(user);
        return user;
    }

    public User updateUser(Long id, UserDto userDto) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundEntityException();
        }
        User user = userRepository.findById(id).get();
        return userRepository.save(new User(
                id,
                userDto.getName() != null ? userDto.getName() : user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail()
        ));
    }

    public void delete(Long userId) {
        userRepository.delete(userRepository.findById(userId).get());
    }
}
