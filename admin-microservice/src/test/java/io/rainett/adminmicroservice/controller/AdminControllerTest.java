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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private AdminServiceImpl adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("getAllAdminsByPage returns AdminDto Page")
    void getAllAdminsByPage_ReturnsAdminDtoPage() throws Exception {
        // Arrange
        AdminDto adminDto1 = new AdminDto(1L, "admin1", "admin_password1", "admin1@gmail.com", null);
        AdminDto adminDto2 = new AdminDto(2L, "admin2", "admin_password2", "admin2@gmail.com", null);
        AdminDto adminDto3 = new AdminDto(3L, "admin3", "admin_password3", "admin3@gmail.com", null);
        AdminDto adminDto4 = new AdminDto(4L, "admin4", "admin_password4", "admin4@gmail.com", null);
        Page<AdminDto> adminDtoPage = new PageImpl<>(List.of(adminDto1, adminDto2, adminDto3, adminDto4));
        when(adminService.getAllAdminsByPage(any(Pageable.class))).thenReturn(adminDtoPage);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].username").value("admin2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].password").value("admin_password3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].email").value("admin4@gmail.com"));
        verify(adminService, times(1)).getAllAdminsByPage(any(Pageable.class));
    }

    @Test
    @DisplayName("getAdminById returns AdminDto when admin was found")
    void getAdminById_ReturnsAdminDto_WhenFound() throws Exception {
        // Arrange
        long id = 1L;
        AdminDto adminDto = new AdminDto(id, "admin1", "admin_password1", "admin1@gmail.com", null);
        when(adminService.getAdminById(id)).thenReturn(adminDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(adminDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(adminDto.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(adminDto.getEmail()));

        verify(adminService, times(1)).getAdminById(id);
    }

    @Test
    @DisplayName("getAdminById throws an exception when admin was not found")
    void getAdminById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 1L;
        when(adminService.getAdminById(id)).thenThrow(new AdminNotFoundException(id));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AdminNotFoundException));
        verify(adminService, times(1)).getAdminById(id);
    }

    @Test
    @DisplayName("updateAdminById returns updated AdminDto")
    void updateAdminById_ReturnsUpdatedAdminDto() throws Exception {
        // Arrange
        long id = 1L;
        AdminDto adminDto = new AdminDto(id, "admin1", "admin_password", "admin1@gmail.com", null);
        when(adminService.updateAdminById(id, adminDto)).thenReturn(adminDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admins/" + id)
                        .content(new ObjectMapper().writeValueAsString(adminDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(adminDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(adminDto.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(adminDto.getEmail()));

        verify(adminService, times(1)).updateAdminById(id, adminDto);
    }

    @Test
    @DisplayName("updateAdminById throws an exception when admin was not found")
    void updateAdminById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 12L;
        AdminDto adminDto = new AdminDto(id, "admin1", "admin_password", "admin1@gmail.com", null);
        when(adminService.updateAdminById(eq(id), any(AdminDto.class))).thenThrow(new AdminNotFoundException(id));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admins/" + id)
                        .content(new ObjectMapper().writeValueAsString(adminDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AdminNotFoundException));
        verify(adminService, times(1)).updateAdminById(eq(id), any(AdminDto.class));
    }

    @Test
    @DisplayName("deleteAdminById returns no content")
    void deleteAdminById_ReturnsNoContent() throws Exception {
        // Arrange
        long id = 2L;
        doNothing().when(adminService).deleteAdminById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/admins/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(adminService, times(1)).deleteAdminById(id);
    }

    @Test
    @DisplayName("deleteAdminById throws an exception when admin was not found")
    void deleteAdminById_ThrowsAnException_WhenNotFound() throws Exception {
        // Arrange
        long id = 2L;
        doThrow(new AdminNotFoundException(id)).when(adminService).deleteAdminById(id);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/admins/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AdminNotFoundException));
        verify(adminService, times(1)).deleteAdminById(id);
    }

}
