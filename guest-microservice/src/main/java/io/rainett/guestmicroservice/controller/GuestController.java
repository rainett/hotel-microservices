package io.rainett.guestmicroservice.controller;

import io.rainett.guestmicroservice.dto.GuestDto;
import io.rainett.guestmicroservice.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public ResponseEntity<Page<GuestDto>> getAllGuestsByPage(Pageable pageable) {
        Page<GuestDto> guestDtoPage = guestService.getAllGuestsByPage(pageable);
        return ResponseEntity.ok(guestDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> getGuestById(@PathVariable Long id) {
        GuestDto guestDto = guestService.getGuestById(id);
        return ResponseEntity.ok(guestDto);
    }

    @PostMapping
    public ResponseEntity<GuestDto> createGuest(@Valid @RequestBody GuestDto guestDto) {
        GuestDto createdGuestDto = guestService.createGuest(guestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestDto> updateGuestById(@PathVariable Long id,
                                                    @Valid @RequestBody GuestDto guestDto) {
        GuestDto updatedGuestDto = guestService.updateGuestById(id, guestDto);
        return ResponseEntity.ok(updatedGuestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuestById(@PathVariable Long id) {
        guestService.deleteGuestById(id);
        return ResponseEntity.noContent().build();
    }

}
