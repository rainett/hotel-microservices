package io.rainett.reservationmicroservice.service.guest;

import org.springframework.kafka.requestreply.RequestReplyFuture;

public interface GuestService {

    RequestReplyFuture<String, String, String> guestCanPerformReservation(Long guestId);

}
