package io.rainett.guestmicroservice.service;

import io.rainett.guestmicroservice.dto.GuestDto;
import io.rainett.guestmicroservice.exception.GuestNotFoundException;
import io.rainett.guestmicroservice.model.Guest;
import io.rainett.guestmicroservice.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Override
    public Page<GuestDto> getAllGuestsByPage(Pageable pageable) {
        return guestRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    public GuestDto getGuestById(Long id) {
        return guestRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new GuestNotFoundException(id));
    }

    @Override
    public GuestDto createGuest(GuestDto guestDto) {
        Guest guest = new Guest();
        return persistGuestAndReturnDto(guestDto, guest);
    }

    @Override
    public GuestDto updateGuestById(Long id, GuestDto guestDto) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
        return persistGuestAndReturnDto(guestDto, guest);
    }

    @Override
    public void deleteGuestById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
        guestRepository.delete(guest);
    }

    private GuestDto persistGuestAndReturnDto(GuestDto guestDto, Guest guest) {
        guest.setFirstName(guestDto.getFirstName());
        guest.setLastName(guestDto.getLastName());
        guest.setEmail(guestDto.getEmail());
        guest.setPhone(guestDto.getPhone());
        guest.setPassword(guestDto.getPassword());
        guest = guestRepository.save(guest);
        return mapToDto(guest);
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
