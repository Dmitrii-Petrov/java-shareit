package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(controllers = BookingController.class)
class BookingControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;






    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookings() {
    }

    @Test
    void getBookingsByOwner() {
    }
}