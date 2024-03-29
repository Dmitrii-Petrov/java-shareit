package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class RequestController {

    RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping()
    public Request create(@RequestBody RequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос POST /requests");
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /requests");
        return requestService.getRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@PathVariable(required = false) Long requestId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /requests/id");
        return requestService.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getRequestAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "1") Integer size) {
        log.info("поулчен запрос GET /requests/all");
        return requestService.getRequestAll(userId, from, size);
    }
}
