package io.rainett.guestmicroservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaListenerService {

    @KafkaListener(topics = "reservation-guest", groupId = "consumer1")
    @SendTo
    public String listenMessages(String in) {
        log.info("Received message ==> {}", in);
        return String.valueOf(true);
    }

}
