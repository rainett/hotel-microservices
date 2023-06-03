package io.rainett.managermicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rainett.managermicroservice.dto.ManagerDto;
import io.rainett.managermicroservice.exception.ManagerNotFoundException;
import io.rainett.managermicroservice.service.ManagerServiceImpl;
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
    private ManagerServiceImpl managerService;

    @InjectMocks
    private ManagerController managerController;

    @InjectMocks
    private ExceptionController exceptionController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController)
                .setControllerAdvice(exceptionController)
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }


    @Test
    @DisplayName("Handles MNFE and returns message")
    public void handleManagerNotFoundException() throws Exception {
        // Arrange
        long id = 3L;
        when(managerService.getManagerById(id)).thenThrow(new ManagerNotFoundException(id));

        // Act and Assert
        String expectedMessage = "Manager with id = [" + id + "] was not found";
        mockMvc.perform(get("/api/v1/managers/" + id)
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
        when(managerService.getManagerById(id)).thenThrow(new RuntimeException(expectedMessage));

        // Act and Assert
        mockMvc.perform(get("/api/v1/managers/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Handles validation error")
    public void handleValidationError() throws Exception {
        // Arrange
        ManagerDto managerDto = ManagerDto.builder()
                .firstName("")
                .lastName("Last")
                .email("not_an_email")
                .password("small")
                .build();

        // Act
        mockMvc.perform(post("/api/v1/managers")
                        .content(new ObjectMapper().writeValueAsBytes(managerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password must be at least 8 characters long"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Invalid email format"));
    }

}
