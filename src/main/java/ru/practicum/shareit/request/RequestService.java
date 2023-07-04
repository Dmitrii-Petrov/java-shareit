package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.practicum.shareit.request.model.RequestMapper.mapToNewRequest;
import static ru.practicum.shareit.request.model.RequestMapper.requestToDto;

@Service("requestService")
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final RequestRepository requestRepository;

    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByUserId(Long userId) {
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        List<Request> list = requestRepository.findByRequesterIdOrderByCreated(userId);
        List<RequestDto> result = new ArrayList<>();
        for (Request request : list) {
            RequestDto requestDto = requestToDto(request);
            requestDto.setItems(itemRepository.findByRequestIdOrderById(request.getId()));
            result.add(requestDto);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public RequestDto getRequestById(Long requestId, Long userId) {
        if (!requestRepository.existsById(requestId)) {
            throw new NotFoundEntityException();
        }
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        RequestDto requestDto = requestToDto(requestRepository.findById(requestId).get());
        requestDto.setItems(itemRepository.findByRequestIdOrderById(requestId));
        return requestDto;
    }


    public Request create(RequestDto requestDto, Long userId) {
        User user = userService.getUsersById(userId).get();
        Request request = mapToNewRequest(requestDto, user);
        return requestRepository.save(request);
    }

    public List<RequestDto> getRequestAll(Long userId, Integer from, Integer size) {
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        List<RequestDto> result = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of(0, size + from, sort);
        Page<Request> requestPage = requestRepository.findAll(page);
        AtomicInteger count = new AtomicInteger(0);
        requestPage.getContent().forEach(request -> {
            if (!request.getRequester().getId().equals(userId)) {
                if (count.get() >= from) {
                    RequestDto requestDto = requestToDto(request);
                    requestDto.setItems(itemRepository.findByRequestIdOrderById(request.getId()));
                    result.add(requestDto);
                    count.set(count.get() + 1);
                }
            }
        });
        return result;

    }
}
