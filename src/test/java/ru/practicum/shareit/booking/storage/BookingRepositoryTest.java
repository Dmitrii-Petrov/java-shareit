package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @Test
    void findByBookerIdOrderByIdDesc() {
    }

    @Test
    void findByBookerIdAndStatusAndEndBeforeOrderByIdDesc() {
    }

    @Test
    void findByItemOwnerOrderByIdDesc() {
    }

    @Test
    void findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerAndStatusOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findByItemIdAndStartAfterAndStatusOrderByStartAsc() {
    }

    @Test
    void findByItemIdAndStartBeforeAndStatusOrderByEndDesc() {
    }
}