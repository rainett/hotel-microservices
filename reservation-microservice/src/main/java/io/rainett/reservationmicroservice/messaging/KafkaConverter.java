package io.rainett.reservationmicroservice.messaging;

import org.springframework.kafka.requestreply.RequestReplyFuture;

public interface KafkaConverter {
    <T> T getValue(RequestReplyFuture<String, String, String> requestReplyFuture, Class<T> castClass);
}
