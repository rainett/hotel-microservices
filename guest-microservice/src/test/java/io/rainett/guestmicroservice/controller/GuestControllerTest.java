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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GuestControllerTest {

    @Mock
    private GuestServiceImpl guestService;

    @InjectMocks
    private GuestController guestController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(guestController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("getAllGuestsByPage returns GuestDto Page")
    void getAllGuestsByPage_ReturnsGuestDtoPage() throws Exception {
        // Arrange
        GuestDto guestDto1 = new GuestDto(1L, "john.doe@example.com", "+1-(123)-456-78-90", "password", "John", "Doe", LocalDateTime.now());
        GuestDto guestDto2 = new GuestDto(2L, "jane.smith@example.com", "+1-(123)-456-78-91", "password", "Jane","Smith", LocalDateTime.now());
        GuestDto guestDto3 = new GuestDto(3L, "mike.johnson@example.com", "+1-(123)-456-78-92", "password", "Mike", "Johnson", LocalDateTime.now());
        GuestDto guestDto4 = new GuestDto(4L, "emily.brown@example.com", "+1-(123)-456-78-93", "password", "Emily", "Brown", LocalDateTime.now());
        Page<GuestDto> guestDtoPage = new PageImpl<>(List.of(guestDto1, guestDto2, guestDto3, guestDto4));
        when(guestService.getAllGuestsByPage(any(Pageable.class))).thenReturn(guestDtoPage);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/guests"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].lastName").value("Johnson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].email").value("emily.brown@example.com"));
        verify(guestService, times(1)).getAllGuestsByPage(any(Pageable.class));
    }

    @Test
    @DisplayName("getGuestById returns GuestDto when guest was found")
    void getGuestById_ReturnsGuestDto_WhenFound() throws Exception {
        // Arrange
        long id = 1L;
        GuestDto guestDto = new GuestDto(id, "John", "Doe", "john.doe@example.com", "johndoe", "password", LocalDateTime.now());
        when(guestService.getGuestById(id)).thenReturn(guestDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/guests/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(guestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(guestDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(guestDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(guestDto.getEmail()));

        verify(guestService, times(1)).getGuestById(id);
    }

    @Test
    @DisplayName("getGuestById throws an exception when guest was not found")
    void getGuestById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 1L;
        when(guestService.getGuestById(id)).thenThrow(new GuestNotFoundException(id));

        // Act and Assert
         mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/guests/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertNotNull(result.getResolvedException()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof GuestNotFoundException));
        verify(guestService, times(1)).getGuestById(id);
    }

    @Test
    @DisplayName("createGuest returns created GuestDto")
    void createGuest_ReturnsCreatedGuestDto() throws Exception {
        // Arrange
        GuestDto guestDto = new GuestDto(null, "john.doe@example.com", "+1-(123)-456-78-90", "password", "John", "password", null);
        GuestDto createdGuestDto = new GuestDto(1L, "john.doe@example.com", "+1-(123)-456-78-90", "password", "John", "password", null);
        when(guestService.createGuest(guestDto)).thenReturn(createdGuestDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/guests")
                        .content(new ObjectMapper().writeValueAsString(guestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdGuestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createdGuestDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createdGuestDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(createdGuestDto.getEmail()));

        verify(guestService, times(1)).createGuest(guestDto);
    }

    @Test
    @DisplayName("updateGuestById returns updated GuestDto")
    void updateGuestById_ReturnsUpdatedGuestDto() throws Exception {
        // Arrange
        long id = 1L;
        GuestDto guestDto = new GuestDto(1L, "john.doe@example.com", "+1-(123)-456-78-90", "password", "John", "password", null);
        GuestDto updatedGuestDto = new GuestDto(1L, "john.doe@example.com", "+1-(123)-456-78-90", "new_password", "John", "password", null);
        when(guestService.updateGuestById(id, guestDto)).thenReturn(updatedGuestDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/guests/" + id)
                        .content(new ObjectMapper().writeValueAsString(guestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedGuestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedGuestDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedGuestDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedGuestDto.getEmail()));

        verify(guestService, times(1)).updateGuestById(id, guestDto);
    }

    @Test
    @DisplayName("deleteGuestById returns no content")
    void deleteGuestById_ReturnsNoContent() throws Exception {
        // Arrange
        long id = 1L;
        doNothing().when(guestService).deleteGuestById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/guests/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(guestService, times(1)).deleteGuestById(id);
    }

    @Test
    @DisplayName("deleteGuestById throws an exception when guest was not found")
    void deleteGuestById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 1L;
        doThrow(new GuestNotFoundException(id)).when(guestService).deleteGuestById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/guests/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertNotNull(result.getResolvedException()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof GuestNotFoundException));

        verify(guestService, times(1)).deleteGuestById(id);
    }
}