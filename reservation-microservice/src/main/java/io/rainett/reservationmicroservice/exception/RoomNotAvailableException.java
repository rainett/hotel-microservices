package io.rainett.reservationmicroservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus()
public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        super("Room with id = [" + roomId +
                "] is not available between checkInDate = [" + checkInDate +
                "] and checkOutDate = [" + checkOutDate +
                "] dates");
    }
}
