package io.rainett.guestmicroservice.service;

import io.rainett.guestmicroservice.dto.GuestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuestService {
    Page<GuestDto> getAllGuestsByPage(Pageable pageable);

    GuestDto getGuestById(Long id);

    GuestDto createGuest(GuestDto guestDto);

    GuestDto updateGuestById(Long id, GuestDto guestDto);

    void deleteGuestById(Long id);
}
