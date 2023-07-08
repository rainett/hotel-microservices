package io.rainett.reservationmicroservice.controller;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import io.rainett.reservationmicroservice.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationDto>> getAllReservationsByPage(Pageable pageable) {
        Page<ReservationDto> reservationDtoPage = reservationService.getAllReservationsByPage(pageable);
        return ResponseEntity.ok(reservationDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationDto);
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        ReservationDto createdReservationDto = reservationService.createReservation(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,
                                                            @Valid @RequestBody ReservationDto reservationDto) {
        ReservationDto updatedReservationDto = reservationService.updateReservation(id, reservationDto);
        return ResponseEntity.ok(updatedReservationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }


}
