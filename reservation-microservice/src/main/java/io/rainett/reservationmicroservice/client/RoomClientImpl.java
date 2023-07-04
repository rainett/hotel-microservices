package io.rainett.reservationmicroservice.client;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomClientImpl implements RoomClient {

    private final ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

    @Override
    public RequestReplyFuture<String, String, String> roomExists(Long roomId) {
        ProducerRecord<String, String> record =
                new ProducerRecord<>("reservation-room", String.valueOf(roomId));
        return kafkaTemplate.sendAndReceive(record);
    }
}
