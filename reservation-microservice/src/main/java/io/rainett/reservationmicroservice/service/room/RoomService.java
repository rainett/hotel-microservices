package io.rainett.reservationmicroservice.service.room;

import org.springframework.kafka.requestreply.RequestReplyFuture;

public interface RoomService {
    RequestReplyFuture<String, String, String> roomIsAvailable(Long roomId);
}
