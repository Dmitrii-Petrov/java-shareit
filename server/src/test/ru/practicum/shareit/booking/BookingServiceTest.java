package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.BookingMapper.mapToNewBooking;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;


    @Test
    void saveBooking() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
//        int size = 10;
//        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        Pageable page = PageRequest.of(0, size + from, sort);
        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId1, item, user1, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user1, item);

        when(itemService.getItemById(bookingDto.getItemId())).thenReturn(item);
        when(userService.getUsersById(userId1)).thenReturn(Optional.of(user1));
        when(itemService.findUserById(userId1)).thenReturn(true);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking response = bookingService.saveBooking(bookingDto, userId1);

        assertEquals(booking, response);
    }

    @Test
    void saveBooking_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;

        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId1, item, user1, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user1, item);

        when(itemService.getItemById(bookingDto.getItemId())).thenReturn(item);
        when(userService.getUsersById(userId1)).thenReturn(Optional.of(user1));
        when(itemService.findUserById(userId1)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.saveBooking(bookingDto, userId1));

        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void saveBooking_whenUserIsItemOwner_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;

        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId1, item, user1, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user1, item);

        when(itemService.getItemById(bookingDto.getItemId())).thenReturn(item);
        when(userService.getUsersById(userId1)).thenReturn(Optional.of(user1));
        when(itemService.findUserById(userId1)).thenReturn(true);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.saveBooking(bookingDto, userId1));

        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void saveBooking_whenItemIsNotAvailable_thenWrongEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(false);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId1, item, user1, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user1, item);

        when(itemService.getItemById(bookingDto.getItemId())).thenReturn(item);
        when(userService.getUsersById(userId1)).thenReturn(Optional.of(user1));
        when(itemService.findUserById(userId1)).thenReturn(true);

        assertThrows(WrongEntityException.class,
                () -> bookingService.saveBooking(bookingDto, userId1));

        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void getBookingById() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId, item, user, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user, item);

        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking response = bookingService.getBookingById(bookingId, userId);

        assertEquals(booking, response);
    }

    @Test
    void getBookingById_whenUserIsItemOwner_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long userId3 = 2L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId, item, user, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user, item);

        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(itemService.findUserById(userId3)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.getBookingById(bookingId, userId3));
    }

    @Test
    void getBookingById_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long userId3 = 2L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(itemService.findUserById(userId3)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.getBookingById(bookingId, userId3));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long userId3 = 2L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto(null, time, time1, itemId, userId, item, user, Status.WAITING);
        Booking booking = mapToNewBooking(bookingDto, user, item);

        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.getBookingById(bookingId, userId3));
    }

    @Test
    void testGetBookings_AllState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdOrderByIdDesc(userId, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.ALL, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_AllStateWithTooBigFrom() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 3;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from / size, size, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdOrderByIdDesc(userId, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.ALL, from, size);

        assertTrue(response.isEmpty());
    }

    @Test
    void testGetBookings_AllStateWithNullSize() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        Integer size = null;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);


        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdOrderByIdDesc(Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.ALL, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_CurrentState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.CURRENT, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_PastState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.PAST, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_FutureState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.FUTURE, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_WaitingState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.WAITING, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookings_RejectedState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookings(userId, BookingState.REJECTED, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }


    @Test
    void testGetBookings_UserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.getBookings(userId, null, from, size));
    }


    @Test
    void testGetBookingsByOwner_AllState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerOrderByIdDesc(userId, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.ALL, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_AllStateWithTooBigFrom() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 3;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from / size, size, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerOrderByIdDesc(userId, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.ALL, from, size);

        assertTrue(response.isEmpty());
    }

    @Test
    void testGetBookingsByOwner_AllStateWithNullSize() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        Integer size = null;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);


        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerOrderByIdDesc(Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.ALL, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_CurrentState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerAndEndAfterAndStartBeforeOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.CURRENT, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_PastState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerAndEndBeforeOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.PAST, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_FutureState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerAndStartAfterOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.FUTURE, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_WaitingState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.WAITING, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.WAITING, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }

    @Test
    void testGetBookingsByOwner_RejectedState() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, Status.REJECTED, page)).thenReturn(bookingPage);

        List<Booking> response = bookingService.getBookingsByOwner(userId, BookingState.REJECTED, from, size);

        assertEquals(booking, response.get(0));
        assertEquals(booking1, response.get(1));
    }


    @Test
    void testGetBookingsByOwner_UserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        int size = 10;
        int from = 0;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.plusDays(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(0, size + from, sort);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setBookerId(userId);
        Booking booking1 = new Booking();
        booking1.setBookerId(userId);

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking, booking1));

        when(itemService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.getBookingsByOwner(userId, null, from, size));
    }

    @Test
    void approve() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);

        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking1)).thenReturn(booking1);

        Booking response = bookingService.approve(bookingId, userId, true);

        assertEquals(booking1, response);
    }

    @Test
    void approve_whenAlreadyApproved_thenWrongEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);

        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(WrongEntityException.class,
                () -> bookingService.approve(bookingId, userId, true));
    }

    @Test
    void approve_whenWantToReject() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.REJECTED);

        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking1)).thenReturn(booking1);

        Booking response = bookingService.approve(bookingId, userId, false);

        assertEquals(booking1, response);
    }

    @Test
    void approve_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);

        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.approve(bookingId, userId, true));
    }

    @Test
    void approve_whenBookingNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);

        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.approve(bookingId, userId, true));
    }

    @Test
    void approve_whenUserIsNotOwner_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        long userId1 = 1L;
        long itemId = 0L;
        long bookingId = 0L;
        User user = new User();
        user.setId(userId);

        User user1 = new User();
        user1.setId(userId1);

        Item item = new Item();
        item.setId(itemId);
        item.setName("name");
        item.setOwner(userId1);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookerId(userId);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        Booking booking1 = new Booking();
        booking1.setId(bookingId);
        booking1.setBookerId(userId);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);

        when(userService.findUserById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundEntityException.class,
                () -> bookingService.approve(bookingId, userId, true));
    }
}