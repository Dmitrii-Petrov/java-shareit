package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.model.RequestMapper.mapToNewRequest;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerIT {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @SneakyThrows
    @Test
    void create() {
        long userId = 0L;
        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item();
        RequestDto requestDto = new RequestDto(0L, "description", user, time, List.of(item));
        Request request = mapToNewRequest(requestDto, user);

        when(requestService.create(requestDto, userId)).thenReturn(request);


        String result = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).create(requestDto, userId);
        assertEquals(objectMapper.writeValueAsString(request), result);
    }


    @SneakyThrows
    @Test
    void getRequests() {
        long userId = 0L;
        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item();
        RequestDto requestDto = new RequestDto(0L, "description", user, time, List.of(item));

        when(requestService.getRequestsByUserId(userId)).thenReturn(List.of(requestDto));

        String result = mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).getRequestsByUserId(userId);
        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);


    }

    @SneakyThrows
    @Test
    void getRequestById() {
        long requestId = 0L;
        long userId = 0L;
        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item();
        RequestDto requestDto = new RequestDto(0L, "description", user, time, List.of(item));

        when(requestService.getRequestById(requestId, userId)).thenReturn(requestDto);

        String result = mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).getRequestById(requestId, userId);
        assertEquals(objectMapper.writeValueAsString(requestDto), result);


    }


    @SneakyThrows
    @Test
    void getRequestAll() {
        long userId = 0L;
        User user = new User();

        LocalDateTime time = LocalDateTime.now();
        Item item = new Item();
        RequestDto requestDto = new RequestDto(0L, "description", user, time, List.of(item));
        when(requestService.getRequestAll(userId, 0, 1)).thenReturn(List.of(requestDto));

        String result = mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).getRequestAll(userId, 0, 1);
        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }
}