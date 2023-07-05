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
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.item.model.CommentMapper.commentToDto;
import static ru.practicum.shareit.item.model.ItemMapper.itemToDto;

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
        long userId = 0L;

        when(userService.findUserById(userId)).thenReturn(true);

        Boolean response = itemService.findUserById(userId);

        assertTrue(response);
    }

    @Test
    void getItemById() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item response = itemService.getItemById(itemId);

        assertEquals(item, response);

    }

    @Test
    void getItemById_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.getItemById(itemId));

        verify(itemRepository, never()).findById(userId);
    }

    @Test
    void getItemDtoByItemId() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();

        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking2));
        when(commentRepository.findByItem_IdOrderByCreatedDesc(itemId)).thenReturn(null);
        when(itemRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto response = itemService.getItemDtoByItemId(itemId, userId);

        assertEquals(item.getId(), response.getId());
        assertEquals(item.getOwner(), response.getOwner());
        assertEquals(item.getName(), response.getName());
        assertEquals(booking1, response.getNextBooking());
        assertEquals(booking2, response.getLastBooking());
    }

    @Test
    void getItemDtoByItemId_whenUserIsNotItemOwner_thenReturnEmpty() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId + 1);

        when(commentRepository.findByItem_IdOrderByCreatedDesc(itemId)).thenReturn(null);
        when(itemRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto response = itemService.getItemDtoByItemId(itemId, userId);

        assertEquals(item.getId(), response.getId());
        assertEquals(item.getOwner(), response.getOwner());
        assertEquals(item.getName(), response.getName());
        assertNull(response.getNextBooking());
        assertNull(response.getLastBooking());
    }

    @Test
    void getItemDtoByItemId_whenNoBookings_thenEmptyItemBookings() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of());
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of());
        when(commentRepository.findByItem_IdOrderByCreatedDesc(itemId)).thenReturn(null);
        when(itemRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto response = itemService.getItemDtoByItemId(itemId, userId);

        assertEquals(item.getId(), response.getId());
        assertEquals(item.getOwner(), response.getOwner());
        assertEquals(item.getName(), response.getName());
        assertNull(response.getLastBooking());
        assertNull(response.getNextBooking());
    }

    @Test
    void getItemDtoByItemId_whenItemHasComment_thenReturnDtoWithComment() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        User user = new User();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();

        Comment comment = new Comment(0L, "text", item, user, LocalDateTime.now());
        CommentDto commentDto = commentToDto(comment);

        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking2));
        when(commentRepository.findByItem_IdOrderByCreatedDesc(itemId)).thenReturn(null);
        when(itemRepository.existsById(userId)).thenReturn(true);
        when(commentRepository.findByItem_IdOrderByCreatedDesc(itemId)).thenReturn(List.of(comment));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto response = itemService.getItemDtoByItemId(itemId, userId);

        assertEquals(item.getId(), response.getId());
        assertEquals(item.getOwner(), response.getOwner());
        assertEquals(item.getName(), response.getName());
        assertEquals(booking1, response.getNextBooking());
        assertEquals(booking2, response.getLastBooking());
        assertEquals(commentDto, response.getComments().get(0));
    }

    @Test
    void create() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(true);
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto response = itemService.create(itemDto, userId);

        assertEquals(item.getId(), response.getId());
        assertEquals(item.getOwner(), response.getOwner());
        assertEquals(item.getName(), response.getName());
    }

    @Test
    void create_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.create(itemDto, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void create_whenRequestNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;
        long requestId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);


        ItemDto itemDto = itemToDto(item);
        itemDto.setRequestId(requestId);

        when(userService.findUserById(userId)).thenReturn(true);
        when(requestRepository.existsById(itemDto.getRequestId())).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.create(itemDto, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void update() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        Item response = itemService.update(itemId, itemDto, userId);

        assertEquals(itemId, response.getId());
        assertEquals(item.getName(), response.getName());
    }

    @Test
    void update_whenDtoDontHasNullsOnOtherFields() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");
        item.setRequestId(1L);
        item.setAvailable(true);

        ItemDto itemDto = itemToDto(item);
        itemDto.setName(null);
        itemDto.setRequestId(0L);


        when(userService.findUserById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        Item response = itemService.update(itemId, itemDto, userId);

        assertEquals(itemId, response.getId());
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getDescription(), response.getDescription());
        assertEquals(item.getRequestId(), response.getRequestId());
    }

    @Test
    void update_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.update(itemId, itemDto, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void update_whenItemNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.update(itemId, itemDto, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void update_whenUserIsNotOwner_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        Item item1 = new Item();
        item1.setOwner(1L);

        ItemDto itemDto = itemToDto(item);

        when(userService.findUserById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));

        assertThrows(NotFoundEntityException.class,
                () -> itemService.update(itemId, itemDto, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void getItemsByTextSearch() {
        long userId = 0L;
        long itemId = 0L;

        String text = "text";

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");

        List<Item> itemList = List.of(item);
        Page<Item> requestPage = new PageImpl<>(itemList);

        when(itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(requestPage);

        List<Item> response = itemService.getItemsByTextSearch(text, 0, 1);

        assertEquals(item, response.get(0));
    }

    @Test
    void getItemsByTextSearch_whenNoFoundItems_thenEmptyAnswer() {
        long userId = 0L;
        long itemId = 0L;

        String text = "text";

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");

        List<Item> itemList = List.of();
        Page<Item> requestPage = new PageImpl<>(itemList);

        when(itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(requestPage);

        List<Item> response = itemService.getItemsByTextSearch(text, 0, 1);

        assertTrue(response.isEmpty());
    }

    @Test
    void getItemsByTextSearch_whenTextIsEmpty_thenEmptyAnswer() {
        long userId = 0L;
        long itemId = 0L;

        String text = "";

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");

        List<Item> response = itemService.getItemsByTextSearch(text, 0, 1);

        assertTrue(response.isEmpty());
    }

    @Test
    void getItemsByTextSearch_whenFromIsTooBig_thenEmptyAnswer() {
        long userId = 0L;
        long itemId = 0L;

        String text = "text";

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");

        List<Item> itemList = List.of(item);
        Page<Item> requestPage = new PageImpl<>(itemList);

        when(itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(requestPage);

        List<Item> response = itemService.getItemsByTextSearch(text, 1, 1);

        assertTrue(response.isEmpty());
    }

    @Test
    void getItemsByTextSearch_whenSizeIsNull() {
        long userId = 0L;
        long itemId = 0L;

        String text = "text";

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setDescription("text");

        List<Item> itemList = List.of(item);
        Page<Item> requestPage = new PageImpl<>(itemList);

        when(itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(requestPage);

        List<Item> response = itemService.getItemsByTextSearch(text, 0, null);

        assertEquals(item, response.get(0));
    }

    @Test
    void addComment() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        User user = new User();

        LocalDateTime time = LocalDateTime.now();

        Comment comment = new Comment(null, "text", item, user, time);
        CommentDto commentDto = commentToDto(comment);
        Booking booking = new Booking();
        booking.setItem(item);


        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStatusAndEndBeforeOrderByIdDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking));
        when(userService.getUsersById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.save(comment)).thenReturn(comment);

        CommentDto response = itemService.addComment(itemId, userId, commentDto);

        assertEquals(commentDto, response);
    }

    @Test
    void addComment_whenItemNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        User user = new User();

        LocalDateTime time = LocalDateTime.now();

        Comment comment = new Comment(null, "text", item, user, time);
        CommentDto commentDto = commentToDto(comment);
        Booking booking = new Booking();
        booking.setItem(item);

        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.addComment(itemId, userId, commentDto));

        verify(commentRepository, never()).save(comment);
    }

    @Test
    void addComment_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        User user = new User();

        LocalDateTime time = LocalDateTime.now();

        Comment comment = new Comment(null, "text", item, user, time);
        CommentDto commentDto = commentToDto(comment);
        Booking booking = new Booking();
        booking.setItem(item);

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> itemService.addComment(itemId, userId, commentDto));

        verify(commentRepository, never()).save(comment);
    }

    @Test
    void addComment_whenUserWasNotBookerOfItem_thenWrongEntityExceptionThrown() {
        long userId = 0L;
        long itemId = 0L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);

        User user = new User();

        LocalDateTime time = LocalDateTime.now();

        Comment comment = new Comment(null, "text", item, user, time);
        CommentDto commentDto = commentToDto(comment);
        Booking booking = new Booking();
        Item item1 = new Item();
        item1.setId(1L);
        booking.setItem(item1);

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStatusAndEndBeforeOrderByIdDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking));

        assertThrows(WrongEntityException.class,
                () -> itemService.addComment(itemId, userId, commentDto));

        verify(commentRepository, never()).save(comment);
    }
}