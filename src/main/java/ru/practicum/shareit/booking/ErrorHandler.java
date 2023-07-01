package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.UnknownStateException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnknownState(UnknownStateException e) {
        return Map.of("error", e.getMessage());
//                new ResponseEntity<>(new UnknownStateError(HttpStatus.BAD_REQUEST.value(), "Unknown state: UNSUPPORTED_STATUS"), HttpStatus.BAD_REQUEST);
    }

}
