package io.rainett.reservationmicroservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaConverterImpl implements KafkaConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public <T> T getValue(RequestReplyFuture<String, String, String> requestReplyFuture, Class<T> castClass) {
        long startTime = System.currentTimeMillis();
        try {
            ConsumerRecord<String, String> result = requestReplyFuture.get();
            log.info("Received response {} in {} ms", result,
                    System.currentTimeMillis() - startTime);
            String value = result.value();
            return MAPPER.readValue(value, castClass);
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
