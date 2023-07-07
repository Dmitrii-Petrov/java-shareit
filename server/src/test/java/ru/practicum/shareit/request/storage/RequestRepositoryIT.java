package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    void findByRequesterIdOrderByCreated() {
        User user = new User(0L, "name", "mail@mail.com");
        userRepository.save(user);
        User user1 = userRepository.findAll().get(0);
        long userID = user1.getId();
        LocalDateTime time = LocalDateTime.now();

        Request request = new Request(null, "description", user1, time);

        requestRepository.save(request);


        List<Request> requestList = requestRepository.findByRequesterIdOrderByCreated(userID);

        assertFalse(requestList.isEmpty());
        assertEquals(requestList.get(0), request);
    }

    @Test
    void findByRequesterIdOrderByCreated_whenWrongUserId_EmptyResult() {
        User user = new User(0L, "name", "mail@mail.com");
        userRepository.save(user);
        User user1 = userRepository.findAll().get(0);
        long userID = user1.getId();
        LocalDateTime time = LocalDateTime.now();

        Request request = new Request(null, "description", user1, time);

        requestRepository.save(request);

        List<Request> requestList = requestRepository.findByRequesterIdOrderByCreated(userID + 1);

        assertTrue(requestList.isEmpty());
    }
}