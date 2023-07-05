package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RequestMapper {

    public static RequestDto requestToDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated(),
                null
        );
    }

    public static Request mapToNewRequest(RequestDto requestDto, User requester) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
