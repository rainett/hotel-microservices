package io.rainett.guestmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rainett.guestmicroservice.dto.GuestDto;
import io.rainett.guestmicroservice.exception.GuestNotFoundException;
import io.rainett.guestmicroservice.service.GuestServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExceptionControllerTest {

    @Mock
    private GuestServiceImpl guestService;

    @InjectMocks
    private GuestController guestController;

    @InjectMocks
    private ExceptionController exceptionController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(guestController)
                .setControllerAdvice(exceptionController)
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }


    @Test
    @DisplayName("Handles MNFE and returns message")
    public void handleGuestNotFoundException() throws Exception {
        // Arrange
        long id = 3L;
        when(guestService.getGuestById(id)).thenThrow(new GuestNotFoundException(id));

        // Act and Assert
        String expectedMessage = "Guest with id = [" + id + "] was not found";
        mockMvc.perform(get("/api/v1/guests/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Handles RuntimeException and returns message")
    public void handleRuntimeException() throws Exception {
        // Arrange
        long id = 3L;
        String expectedMessage = "Such an error! 0_0";
        when(guestService.getGuestById(id)).thenThrow(new RuntimeException(expectedMessage));

        // Act and Assert
        mockMvc.perform(get("/api/v1/guests/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Handles validation error")
    public void handleValidationError() throws Exception {
        // Arrange
        GuestDto guestDto = GuestDto.builder()
                .firstName("")
                .lastName("Last")
                .email("not_an_email")
                .phone("aboba")
                .password("small")
                .build();

        // Act
        mockMvc.perform(post("/api/v1/guests")
                        .content(new ObjectMapper().writeValueAsBytes(guestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password must be at least 8 characters long"))
                .andExpect(jsonPath("$.fieldErrors.phone").value("Invalid phone number format"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Invalid email format"));
    }

}
