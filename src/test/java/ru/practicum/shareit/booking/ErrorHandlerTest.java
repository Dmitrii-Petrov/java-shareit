package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exceptions.UnknownStateException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleUnknownStateTest() {
        UnknownStateException e = new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
        Map<String, String> error = errorHandler.handleUnknownState(e);

        assertFalse(error.isEmpty());
        assertEquals(error.get("error"), e.getMessage());
    }
}