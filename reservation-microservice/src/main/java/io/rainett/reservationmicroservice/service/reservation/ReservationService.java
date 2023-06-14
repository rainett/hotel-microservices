package io.rainett.reservationmicroservice.service.reservation;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    Page<ReservationDto> getAllReservationsByPage(Pageable pageable);

    ReservationDto getReservationById(Long id);

    ReservationDto createReservation(ReservationDto reservationDto);

    ReservationDto updateReservation(Long id, ReservationDto reservationDto);

    void deleteReservation(Long id);

    ReservationDto guestReservation(ReservationDto reservationDto);

    void cancelReservation(Long id);
}
