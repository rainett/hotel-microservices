package io.rainett.reservationmicroservice.service.room;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

    @Override
    public RequestReplyFuture<String, String, String> roomIsAvailable(Long roomId) {
        ProducerRecord<String, String> record =
                new ProducerRecord<>("reservation-room", String.valueOf(roomId));
        return kafkaTemplate.sendAndReceive(record);
    }
}
