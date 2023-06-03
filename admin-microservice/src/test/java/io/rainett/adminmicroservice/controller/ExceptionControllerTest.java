package io.rainett.adminmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rainett.adminmicroservice.dto.AdminDto;
import io.rainett.adminmicroservice.exception.AdminNotFoundException;
import io.rainett.adminmicroservice.service.AdminServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExceptionControllerTest {

    @Mock
    private AdminServiceImpl adminService;

    @InjectMocks
    private AdminController adminController;

    @InjectMocks
    private ExceptionController exceptionController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(exceptionController)
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }


    @Test
    @DisplayName("Handles ANFE and returns message")
    public void handleAdminNotFoundException() throws Exception {
        // Arrange
        long id = 3L;
        when(adminService.getAdminById(id)).thenThrow(new AdminNotFoundException(id));

        // Act and Assert
        String expectedMessage = "Admin with id = [" + id + "] was not found";
        mockMvc.perform(get("/api/v1/admins/" + id)
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
        when(adminService.getAdminById(id)).thenThrow(new RuntimeException(expectedMessage));

        // Act and Assert
        mockMvc.perform(get("/api/v1/admins/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Handles validation error")
    public void handleValidationError() throws Exception {
        // Arrange
        AdminDto adminDto = AdminDto.builder()
                .username("")
                .email("not_an_email")
                .password("small")
                .build();

        // Act
        mockMvc.perform(put("/api/v1/admins/1")
                        .content(new ObjectMapper().writeValueAsBytes(adminDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors.username").value("Username is required"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password must be at least 8 characters long"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Invalid email format"));
    }

}
