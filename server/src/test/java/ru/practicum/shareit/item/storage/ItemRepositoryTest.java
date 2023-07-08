package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {


    Long userID;
    Long requestId;
    Item item;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void addItems() {

        User user = new User(0L, "name", "mail@mail.com");
        userRepository.save(user);
        User user1 = userRepository.findAll().get(0);

        userID = user1.getId();

        Item item1 = new Item(0L, "name", "description", true, user1.getId(), 2L);
        item = itemRepository.save(item1);
        requestId = item.getRequestId();
    }

    @Test
    void findByOwnerOrderById() {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, 10, sort);
        Page<Item> itemPage = itemRepository.findByOwnerOrderById(userID, page);

        assertFalse(itemPage.getContent().isEmpty());
        assertEquals(itemPage.getContent().get(0), item);
    }

    @Test
    void findByOwnerOrderById_whenWrongUserId_EmptyResult() {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, 10, sort);
        Page<Item> itemPage = itemRepository.findByOwnerOrderById(userID + 1, page);

        assertTrue(itemPage.getContent().isEmpty());
    }

    @Test
    void findByRequestIdOrderById() {
        List<Item> list = itemRepository.findByRequestIdOrderById(requestId);

        assertFalse(list.isEmpty());
        assertEquals(list.get(0), item);
    }

    @Test
    void findByRequestIdOrderById_whenWrongRequestId_EmptyResult() {
        List<Item> list = itemRepository.findByRequestIdOrderById(requestId + 1);

        assertTrue(list.isEmpty());
    }

    @Test
    void findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase() {
        String search = "na";
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, 10, sort);
        Page<Item> itemPage = itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(true, search, true, search, page);

        assertFalse(itemPage.getContent().isEmpty());
        assertEquals(itemPage.getContent().get(0), item);

    }


    @Test
    void findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase_whenNoMatch_EmptyResult() {
        String search = "qe";
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, 10, sort);
        Page<Item> itemPage = itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(true, search, true, search, page);

        assertTrue(itemPage.getContent().isEmpty());
    }


    @AfterEach
    private void deleteItems() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }
}