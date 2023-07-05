package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.request.model.RequestMapper.mapToNewRequest;
import static ru.practicum.shareit.request.model.RequestMapper.requestToDto;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    @InjectMocks
    private RequestMapper requestMapper;

    @Test
    void requestToDtoTest() {
        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        Request request = new Request(0L,"text",user,time);

        RequestDto requestDto = requestToDto(request);

        assertEquals(requestDto.getId(), request.getId());
        assertEquals(requestDto.getDescription(), request.getDescription());
        assertEquals(requestDto.getRequestor(), request.getRequester());
        assertEquals(requestDto.getCreated(), request.getCreated());
    }

    @Test
    void mapToNewRequestTest() {
        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item();
        RequestDto requestDto = new RequestDto(0L,"text",user,time, List.of(item));

        Request request = mapToNewRequest(requestDto,user);

        assertEquals(requestDto.getDescription(), request.getDescription());
        assertEquals(requestDto.getRequestor(), request.getRequester());
        assertEquals(requestDto.getItems().get(0), item);
    }
}