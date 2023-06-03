package io.rainett.managermicroservice.service;

import io.rainett.managermicroservice.dto.ManagerDto;
import io.rainett.managermicroservice.exception.ManagerNotFoundException;
import io.rainett.managermicroservice.model.Manager;
import io.rainett.managermicroservice.repository.ManagerRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

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
    @DisplayName("getAllManagersByPage returns page of managers")
    void getAllManagersByPage_ReturnsPageOfManagers() {
        // Arrange
        Manager manager1 = new Manager(1L, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now());
        Manager manager2 = new Manager(2L, "Jane", "Smith", "jane.smith@example.com", "password", LocalDateTime.now());
        Manager manager3 = new Manager(3L, "David", "Johnson", "david.johnson@example.com", "password", LocalDateTime.now());
        Manager manager4 = new Manager();
        Page<Manager> managerPage = new PageImpl<>(List.of(manager1, manager2, manager3, manager4));
        when(managerRepository.findAll(any(Pageable.class))).thenReturn(managerPage);

        // Act
        Page<ManagerDto> result = managerService.getAllManagersByPage(Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(managerPage.getSize(), result.getSize());
    }

    @Test
    @DisplayName("getManagerById returns manager when it was found")
    void getManagerById_ReturnsManagerDto_WhenFound() {
        // Arrange
        long id = 1L;
        Manager manager = new Manager(id, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now());
        when(managerRepository.findById(id)).thenReturn(Optional.of(manager));

        // Act
        ManagerDto result = managerService.getManagerById(id);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(manager), result);
    }

    @Test
    @DisplayName("getManagerById throws an exception when manager was not found")
    void getManagerById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 1L;
        when(managerRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ManagerNotFoundException.class, () -> managerService.getManagerById(id));
    }

    @Test
    @DisplayName("createManager creates manager and returns DTO")
    void createManager_CreatesManager_AndReturnsDto() {
        // Arrange
        ManagerDto managerDto = ManagerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        Manager createdManager = new Manager(1L, "John", "Doe", "john.doe@example.com", "password", null);
        when(managerRepository.save(any(Manager.class))).thenReturn(createdManager);

        // Act
        ManagerDto result = managerService.createManager(managerDto);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(createdManager), result);
    }

    @Test
    @DisplayName("updateManagerById returns updated managerDTO")
    void updateManagerById_ReturnsUpdatedManagerDto() {
        // Arrange
        ManagerDto managerDto = ManagerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();
        long id = 1L;
        Manager manager = new Manager(id, "John", "Doe", "john.doe@example.com", "password", null);
        Manager updatedManager = new Manager(id, "John", "Doe", "johndoe@example.com", "password", null);
        when(managerRepository.findById(id)).thenReturn(Optional.of(manager));
        when(managerRepository.save(any(Manager.class))).thenReturn(updatedManager);

        // Act
        ManagerDto result = managerService.updateManagerById(id, managerDto);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(updatedManager), result);
    }

    @Test
    @DisplayName("updateManagerById throws an exception when manager was not found")
    void updateManagerById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 12L;
        ManagerDto managerDto = ManagerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();
        when(managerRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ManagerNotFoundException.class, () -> managerService.updateManagerById(id, managerDto));
    }

    @Test
    @DisplayName("deleteManagerById deletes manager")
    void deleteManagerById_DeletesManager() {
        // Arrange
        long id = 12L;
        Manager manager = new Manager(id, "John", "Doe", "john.doe@example.com", "password", null);
        when(managerRepository.findById(id)).thenReturn(Optional.of(manager));

        // Act
        managerService.deleteManagerById(id);

        // Assert
        verify(managerRepository, times(1)).delete(manager);
    }

    @Test
    @DisplayName("deleteManagerById throws an exception when manager was not found")
    void deleteManagerById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 13L;
        when(managerRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ManagerNotFoundException.class, () -> managerService.deleteManagerById(id));
    }


    private ManagerDto mapToDto(Manager manager) {
        return ManagerDto.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .email(manager.getEmail())
                .password(manager.getPassword())
                .createdAt(manager.getCreatedAt())
                .build();
    }


}