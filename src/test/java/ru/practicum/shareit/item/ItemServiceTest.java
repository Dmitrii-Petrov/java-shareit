package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;


    @Test
    void getItemsByUserId() {
        long userId = 0L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        LocalDateTime time = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, size + from, sort);
        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(itemRepository.findByOwnerOrderById(userId, page)).thenReturn(itemPage);
        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking2));

        List<ItemDto> response = itemService.getItemsByUserId(userId, from, size);

        assertEquals(response.get(0).getName(), item.getName());
    }

    @Test
    void getItemsByUserId_whenNextAndLastBookingEmpty() {
        long userId = 0L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        LocalDateTime time = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, size + from, sort);
        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(itemRepository.findByOwnerOrderById(userId, page)).thenReturn(itemPage);
        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of());
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of());

        List<ItemDto> response = itemService.getItemsByUserId(userId, from, size);

        assertEquals(response.get(0).getName(), item.getName());
        assertNull(response.get(0).getLastBooking());
        assertNull(response.get(0).getNextBooking());
    }

    @Test
    void getItemsByUserId_whenFromIsTooBig_thenReturnEmptyList() {
        long userId = 0L;
        long itemId = 0L;
        int size = 10;
        int from = 2;
        LocalDateTime time = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, size + from, sort);
        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(itemRepository.findByOwnerOrderById(userId, page)).thenReturn(itemPage);

        List<ItemDto> response = itemService.getItemsByUserId(userId, from, size);

        assertTrue(response.isEmpty());
    }

    @Test
    void getItemsByUserId_whenSizeIsNull() {
        long userId = 0L;
        long itemId = 0L;
        Integer size = null;
        Integer from = 0;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(itemRepository.findByOwnerOrderById(Mockito.any(), Mockito.any())).thenReturn(itemPage);
        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking2));

        List<ItemDto> response = itemService.getItemsByUserId(userId, from, size);

        assertEquals(response.size(), 1);
    }

    @Test
    void findUserById() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void getItemDtoByItemId() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getItemsByTextSearch() {
    }

    @Test
    void addComment() {
    }
}