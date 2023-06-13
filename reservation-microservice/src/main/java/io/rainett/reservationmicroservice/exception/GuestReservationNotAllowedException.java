package io.rainett.reservationmicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class GuestReservationNotAllowedException extends RuntimeException {
    public GuestReservationNotAllowedException(Long guestId) {
        super("Guest with id = [" + guestId + "] is not allowed to perform reservations");
    }
}
