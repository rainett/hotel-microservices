package io.rainett.reservationmicroservice.exception;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus()
public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(ReservationDto reservationDto) {
        super("Room with id = [" + reservationDto.getRoomId() +
                "] is not available between checkInDate = [" + reservationDto.getCheckInDate() +
                "] and checkOutDate = [" + reservationDto.getCheckOutDate() +
                "] dates");
    }
}
