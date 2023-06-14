package io.rainett.roommicroservice.listener;

import io.rainett.roommicroservice.model.Room;
import io.rainett.roommicroservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    private final RoomRepository roomRepository;

    @KafkaListener(topics = "reservation-room", groupId = "consumer1")
    @SendTo
    public String roomIsAvailable(String in) {
        log.info("Received message ==> {}", in);
        Long roomId = Long.parseLong(in);
        Double reservation = roomRepository.findById(roomId)
                .map(Room::getReservationPrice)
                .map(BigDecimal::doubleValue)
                .orElse(-1.0);
        return String.valueOf(reservation);
    }


}
