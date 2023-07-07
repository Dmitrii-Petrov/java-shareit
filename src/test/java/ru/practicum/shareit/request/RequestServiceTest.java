package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.request.model.RequestMapper.requestToDto;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private RequestRepository requestRepository;


    @Test
    void getRequestsByUserId_whenUserFound_thenReturnList() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);

        ArrayList<Request> list = new ArrayList<>();
        list.add(request);

        when(userService.findUserById(userId)).thenReturn(true);
        when(requestRepository.findByRequesterIdOrderByCreated(userId)).thenReturn(list);
        when(itemRepository.findByRequestIdOrderById(request.getId())).thenReturn(itemList);
        List<RequestDto> response = requestService.getRequestsByUserId(userId);

        assertEquals(requestDto, response.get(0));
        verify(requestRepository).findByRequesterIdOrderByCreated(userId);
        verify(itemRepository).findByRequestIdOrderById(request.getId());
    }

    @Test
    void getRequestsByUserId_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;

        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> requestService.getRequestsByUserId(userId));

        verify(requestRepository, never()).findByRequesterIdOrderByCreated(userId);
        verify(itemRepository, never()).findByRequestIdOrderById(anyLong());
    }


    @Test
    void getRequestById() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        long requestId = 1L;
        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);

        when(requestRepository.existsById(requestId)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(true);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestIdOrderById(requestId)).thenReturn(itemList);
        RequestDto response = requestService.getRequestById(requestId, userId);

        assertEquals(response, requestDto);
        verify(requestRepository).findById(requestId);
        verify(itemRepository).findByRequestIdOrderById(requestId);
    }

    @Test
    void getRequestById_whenRequestNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        long requestId = 1L;
        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);

        when(requestRepository.existsById(requestId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> requestService.getRequestById(requestId, userId));

        verify(requestRepository, never()).findById(requestId);
        verify(itemRepository, never()).findByRequestIdOrderById(requestId);
    }

    @Test
    void getRequestById_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        long requestId = 1L;
        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);

        when(requestRepository.existsById(requestId)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(false);


        assertThrows(NotFoundEntityException.class,
                () -> requestService.getRequestById(requestId, userId));

        verify(requestRepository, never()).findById(requestId);
        verify(itemRepository, never()).findByRequestIdOrderById(requestId);
    }

    @Test
    void create() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);
        LocalDateTime created = LocalDateTime.now();
        requestDto.setCreated(created);
        request.setCreated(created);

        when(userService.getUsersById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(Mockito.any())).thenReturn(request);

        Request response = requestService.create(requestDto, userId);

        assertEquals(response, request);
        verify(userService).getUsersById(userId);
        verify(requestRepository).save(Mockito.any());
    }

    @Test
    void getRequestAll_whenUserFoundAndAskedByRequestOwner_thenReturnEmptyList() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);
        LocalDateTime created = LocalDateTime.now();
        requestDto.setCreated(created);
        request.setCreated(created);

        List<Request> requestList = List.of(request);
        Page<Request> requestPage = new PageImpl<>(requestList);

        when(userService.findUserById(userId)).thenReturn(true);
        when(requestRepository.findAll((Pageable) Mockito.any())).thenReturn(requestPage);

        List<RequestDto> response = requestService.getRequestAll(userId, 0, 10);

        assertTrue(response.isEmpty());
        verify(userService).findUserById(userId);
        verify(requestRepository).findAll((Pageable) Mockito.any());
    }

    @Test
    void getRequestAll_whenFromIsTooHigh_thenReturnEmptyList() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);
        LocalDateTime created = LocalDateTime.now();
        requestDto.setCreated(created);
        request.setCreated(created);

        List<Request> requestList = List.of(request);
        Page<Request> requestPage = new PageImpl<>(requestList);

        when(userService.findUserById(userId + 1)).thenReturn(true);
        when(requestRepository.findAll((Pageable) Mockito.any())).thenReturn(requestPage);

        List<RequestDto> response = requestService.getRequestAll(userId + 1, 1, 10);

        assertTrue(response.isEmpty());
        verify(userService).findUserById(userId + 1);
        verify(requestRepository).findAll((Pageable) Mockito.any());
    }

    @Test
    void getRequestAll_whenUserFoundAndAskedByAnotherUser_thenReturnList() {
        long userId = 0L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(userId);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Request request = new Request();
        request.setRequester(user);
        RequestDto requestDto = requestToDto(request);
        requestDto.setItems(itemList);
        LocalDateTime created = LocalDateTime.now();
        requestDto.setCreated(created);
        request.setCreated(created);

        List<Request> requestList = List.of(request);
        Page<Request> requestPage = new PageImpl<>(requestList);

        when(userService.findUserById(userId + 1)).thenReturn(true);
        when(requestRepository.findAll((Pageable) Mockito.any())).thenReturn(requestPage);
        when(itemRepository.findByRequestIdOrderById(request.getId())).thenReturn(itemList);

        List<RequestDto> response = requestService.getRequestAll(userId + 1, 0, 10);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0), requestDto);
        verify(userService).findUserById(userId + 1);
        verify(requestRepository).findAll((Pageable) Mockito.any());
    }

    @Test
    void getRequestAll_whenUserNotFound_thenNotFoundEntityExceptionThrown() {
        long userId = 0L;

        when(userService.findUserById(userId)).thenReturn(false);

        assertThrows(NotFoundEntityException.class,
                () -> requestService.getRequestAll(userId, 0, 10));

        verify(requestRepository, never()).findAll((Pageable) Mockito.any());
        verify(itemRepository, never()).findByRequestIdOrderById(anyLong());

    }
}