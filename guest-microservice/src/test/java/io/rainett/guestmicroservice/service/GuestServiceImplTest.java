package io.rainett.guestmicroservice.service;

import io.rainett.guestmicroservice.dto.GuestDto;
import io.rainett.guestmicroservice.exception.GuestNotFoundException;
import io.rainett.guestmicroservice.model.Guest;
import io.rainett.guestmicroservice.repository.GuestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    @DisplayName("getAllGuestsByPage returns page of guests")
    void getAllGuestsByPage_ReturnsPageOfGuests() {
        // Arrange
        Guest guest1 = new Guest(1L, "email1@mail.com", "+1-(123)-123-45-67", "password1", "name1", "surname1", LocalDateTime.now());
        Guest guest2 = new Guest(2L, "email2@mail.com", "+1-(124)-123-45-67", "password2", "name2", "surname2", LocalDateTime.now());
        Guest guest3 = new Guest(3L, "email3@mail.com", "+1-(125)-123-45-67", "password3", "name3", "surname3", LocalDateTime.now());
        Guest guest4 = new Guest(4L, "email4@mail.com", "+1-(126)-123-45-67", "password4", "name4", "surname4", LocalDateTime.now());
        Page<Guest> guestPage = new PageImpl<>(List.of(guest1, guest2, guest3, guest4));
        Mockito.when(guestRepository.findAll(Mockito.any(Pageable.class))).thenReturn(guestPage);

        // Act
        Page<GuestDto> result = guestService.getAllGuestsByPage(Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(guestPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("getGuestById returns guest when it was found")
    void getGuestById_ReturnsGuestDto_WhenFound() {
        // Arrange
        long id = 1L;
        Guest guest = new Guest(id, "John", "Doe", "john.doe@example.com", "+1-(123)-45-67-890", "password", LocalDateTime.now());
        when(guestRepository.findById(id)).thenReturn(Optional.of(guest));

        // Act
        GuestDto result = guestService.getGuestById(id);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(guest), result);
    }

    @Test
    @DisplayName("getGuestById throws an exception when guest was not found")
    void getGuestById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 1L;
        when(guestRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(GuestNotFoundException.class, () -> guestService.getGuestById(id));
    }

    @Test
    @DisplayName("createGuest creates guest and returns DTO")
    void createGuest_CreatesGuest_AndReturnsDto() {
        // Arrange
        GuestDto guestDto = GuestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        Guest createdGuest = new Guest(1L, "John", "Doe", "john.doe@example.com", "+1-(123)-45-67-890", "password", null);
        when(guestRepository.save(any(Guest.class))).thenReturn(createdGuest);

        // Act
        GuestDto result = guestService.createGuest(guestDto);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(createdGuest), result);
    }

    @Test
    @DisplayName("updateGuestById returns updated guestDTO")
    void updateGuestById_ReturnsUpdatedGuestDto() {
        // Arrange
        GuestDto guestDto = GuestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();
        long id = 1L;
        Guest guest = new Guest(id, "John", "Doe", "john.doe@example.com", "johndoe", "password", null);
        Guest updatedGuest = new Guest(id, "John", "Doe", "johndoe@example.com", "johndoe", "password", null);
        when(guestRepository.findById(id)).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(updatedGuest);

        // Act
        GuestDto result = guestService.updateGuestById(id, guestDto);

        // Assert
        assertNotNull(result);
        assertEquals(mapToDto(updatedGuest), result);
    }

    @Test
    @DisplayName("updateGuestById throws an exception when guest was not found")
    void updateGuestById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 12L;
        GuestDto guestDto = GuestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();
        when(guestRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(GuestNotFoundException.class, () -> guestService.updateGuestById(id, guestDto));
    }

    @Test
    @DisplayName("deleteGuestById deletes guest")
    void deleteGuestById_DeletesGuest() {
        // Arrange
        long id = 12L;
        Guest guest = new Guest(id, "John", "Doe", "john.doe@example.com", "johndoe", "password", null);
        when(guestRepository.findById(id)).thenReturn(Optional.of(guest));

        // Act
        guestService.deleteGuestById(id);

        // Assert
        verify(guestRepository, times(1)).delete(guest);
    }

    @Test
    @DisplayName("deleteGuestById throws an exception when guest was not found")
    void deleteGuestById_ThrowsAnException_WhenNotFound() {
        // Arrange
        long id = 13L;
        when(guestRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(GuestNotFoundException.class, () -> guestService.deleteGuestById(id));
    }


    private GuestDto mapToDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .password(guest.getPassword())
                .createdAt(guest.getCreatedAt())
                .build();
    }


}