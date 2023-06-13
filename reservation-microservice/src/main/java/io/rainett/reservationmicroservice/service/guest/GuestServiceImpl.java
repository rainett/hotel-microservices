package io.rainett.reservationmicroservice.service.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuestServiceImpl implements GuestService {

    private final ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

    public RequestReplyFuture<String, String, String> guestCanPerformReservation(Long guestId) {
        ProducerRecord<String, String> record =
                new ProducerRecord<>("reservation-guest", String.valueOf(guestId));
        return kafkaTemplate.sendAndReceive(record);
    }

}
