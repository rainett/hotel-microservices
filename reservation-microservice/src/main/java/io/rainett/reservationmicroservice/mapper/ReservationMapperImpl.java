package io.rainett.reservationmicroservice.mapper;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import io.rainett.reservationmicroservice.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapperImpl implements ReservationMapper {
    @Override
    public ReservationDto toReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .roomId(reservation.getRoomId())
                .guestId(reservation.getGuestId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}
