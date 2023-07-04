package io.rainett.reservationmicroservice.client;

import org.springframework.kafka.requestreply.RequestReplyFuture;

public interface RoomClient {
    RequestReplyFuture<String, String, String> roomExists(Long roomId);
}
