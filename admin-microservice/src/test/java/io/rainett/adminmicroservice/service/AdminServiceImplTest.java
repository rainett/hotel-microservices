package io.rainett.adminmicroservice.service;

import io.rainett.adminmicroservice.dto.AdminDto;
import io.rainett.adminmicroservice.exception.AdminNotFoundException;
import io.rainett.adminmicroservice.model.Admin;
import io.rainett.adminmicroservice.repository.AdminRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void destroy() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("getAllAdminsByPage returns page of admins")
    void getAllAdminsByPage_ReturnsPageOfAdmins() {
        // Arrange
        Admin admin1 = new Admin(1L, "admin1", "admin_password1", "admin1@gmail.com");
        Admin admin2 = new Admin(2L, "admin2", "admin_password2", "admin2@gmail.com");
        Admin admin3 = new Admin(3L, "admin3", "admin_password3", "admin3@gmail.com");
        Admin admin4 = new Admin();
        Page<Admin> adminPage = new PageImpl<>(List.of(admin1, admin2, admin3, admin4));
        System.out.println(admin1);
        when(adminRepository.findAll(any(Pageable.class))).thenReturn(adminPage);

        // Act
        Page<AdminDto> result = adminService.getAllAdminsByPage(Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(adminPage.getSize(), result.getSize());
    }

    @Test
    @DisplayName("getAdminById returns admin by id when it was found")
    void getAdminById_ReturnsAdminById_WhenFound() {
        // Arrange
        long id = 23L;
        Admin admin = new Admin(id, "admin", "admin_password", "admin@gmail.com");
        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        // Act
        AdminDto result = adminService.getAdminById(id);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(admin), result);
    }

    @Test
    @DisplayName("getAdminById throws an exception when admin was not found")
    void getAdminById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 2L;
        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminById(id));
    }

    @Test
    @DisplayName("updateAdminById updates admin by ID and returns updated admin")
    void updateAdminById_UpdatesAdminById_AndReturnsUpdatedAdmin() {
        // Arrange
        Long id = 23L;
        Admin initialAdmin = new Admin(id, "admin", "admin_password", "admin@gmail.com");
        Admin updatedAdmin = new Admin(id, "updated_admin", "new_password", "ad@gmail.com");
        AdminDto updatedAdminDto = new AdminDto(id, "updated_admin", "new_password", "ad@gmail.com");
        when(adminRepository.findById(id)).thenReturn(Optional.of(initialAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        // Act
        AdminDto result = adminService.updateAdminById(id, updatedAdminDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedAdminDto, result);
    }

    @Test
    @DisplayName("updateAdminById throws an exception when admin was not found")
    void updateAdminById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 34L;
        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.updateAdminById(id, new AdminDto()));
    }

    @Test
    @DisplayName("deleteAdminById deletes the admin")
    void deleteAdminById_DeletesAdmin() {
        // Arrange
        long id = 12L;
        Admin admin = new Admin(id, "admin", "admin_password", "admin@gmail.com");
        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        // Act
        adminService.deleteAdminById(id);

        // Assert
        verify(adminRepository, times(1)).delete(admin);

    }

    @Test
    @DisplayName("deleteAdminById throws an exception when admin was not found")
    void deleteAdminById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 34L;
        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.deleteAdminById(id));
    }

    private AdminDto mapToDto(Admin admin) {
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .email(admin.getEmail())
                .build();
    }
}