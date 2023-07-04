package io.rainett.reservationmicroservice.mapper;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import io.rainett.reservationmicroservice.model.Reservation;

public interface ReservationMapper {
    ReservationDto toReservationDto(Reservation reservation);
}
