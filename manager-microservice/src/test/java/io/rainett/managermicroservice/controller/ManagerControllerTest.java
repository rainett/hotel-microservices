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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerControllerTest {

    @Mock
    private ManagerServiceImpl managerService;

    @InjectMocks
    private ManagerController managerController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("getAllManagersByPage returns ManagerDto Page")
    void getAllManagersByPage_ReturnsManagerDtoPage() throws Exception {
        // Arrange
        ManagerDto managerDto1 = new ManagerDto(1L, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now());
        ManagerDto managerDto2 = new ManagerDto(2L, "Jane", "Smith", "jane.smith@example.com", "password", LocalDateTime.now());
        ManagerDto managerDto3 = new ManagerDto(3L, "Mike", "Johnson", "mike.johnson@example.com", "password", LocalDateTime.now());
        ManagerDto managerDto4 = new ManagerDto(4L, "Emily", "Brown", "emily.brown@example.com", "password", LocalDateTime.now());
        Page<ManagerDto> managerDtoPage = new PageImpl<>(List.of(managerDto1, managerDto2, managerDto3, managerDto4));
        when(managerService.getAllManagersByPage(any(Pageable.class))).thenReturn(managerDtoPage);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].lastName").value("Johnson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].email").value("emily.brown@example.com"));
        verify(managerService, times(1)).getAllManagersByPage(any(Pageable.class));
    }

    @Test
    @DisplayName("getManagerById returns ManagerDto when manager was found")
    void getManagerById_ReturnsManagerDto_WhenFound() throws Exception {
        // Arrange
        long id = 1L;
        ManagerDto managerDto = new ManagerDto(id, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now());
        when(managerService.getManagerById(id)).thenReturn(managerDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(managerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(managerDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(managerDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(managerDto.getEmail()));

        verify(managerService, times(1)).getManagerById(id);
    }

    @Test
    @DisplayName("getManagerById throws an exception when manager was not found")
    void getManagerById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 1L;
        when(managerService.getManagerById(id)).thenThrow(new ManagerNotFoundException(id));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertNotNull(result.getResolvedException()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerNotFoundException));
        verify(managerService, times(1)).getManagerById(id);
    }

    @Test
    @DisplayName("createManager returns created ManagerDto")
    void createManager_ReturnsCreatedManagerDto() throws Exception {
        // Arrange
        ManagerDto managerDto = new ManagerDto(null, "John", "Doe", "john.doe@example.com", "password", null);
        ManagerDto createdManagerDto = new ManagerDto(1L, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now());
        when(managerService.createManager(managerDto)).thenReturn(createdManagerDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/managers")
                        .content(new ObjectMapper().writeValueAsString(managerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdManagerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createdManagerDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createdManagerDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(createdManagerDto.getEmail()));

        verify(managerService, times(1)).createManager(managerDto);
    }

    @Test
    @DisplayName("updateManagerById returns updated ManagerDto")
    void updateManagerById_ReturnsUpdatedManagerDto() throws Exception {
        // Arrange
        long id = 1L;
        ManagerDto managerDto = new ManagerDto(id, "John", "Doe", "john.doe@example.com", "password", null);
        ManagerDto updatedManagerDto = new ManagerDto(id, "John", "Doe", "john.doe@example.com", "new_password", LocalDateTime.now());
        when(managerService.updateManagerById(id, managerDto)).thenReturn(updatedManagerDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/managers/" + id)
                        .content(new ObjectMapper().writeValueAsString(managerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedManagerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedManagerDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedManagerDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedManagerDto.getEmail()));

        verify(managerService, times(1)).updateManagerById(id, managerDto);
    }

    @Test
    @DisplayName("deleteManagerById returns no content")
    void deleteManagerById_ReturnsNoContent() throws Exception {
        // Arrange
        long id = 1L;
        doNothing().when(managerService).deleteManagerById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/managers/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(managerService, times(1)).deleteManagerById(id);
    }

    @Test
    @DisplayName("deleteManagerById throws an exception when manager was not found")
    void deleteManagerById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 1L;
        doThrow(new ManagerNotFoundException(id)).when(managerService).deleteManagerById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/managers/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertNotNull(result.getResolvedException()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerNotFoundException));

        verify(managerService, times(1)).deleteManagerById(id);
    }
}
