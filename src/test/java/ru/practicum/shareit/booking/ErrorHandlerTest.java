package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.request.RequestService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleUnknownStateTest() {
        UnknownStateException e = new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        Map<String,String> error = errorHandler.handleUnknownState(e);

        assertFalse(error.isEmpty());
        assertEquals(error.get("error"), e.getMessage());
    }
}