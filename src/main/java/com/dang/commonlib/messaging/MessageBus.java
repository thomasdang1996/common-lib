package com.dang.commonlib.messaging;

import com.dang.commonlib.exception.MissingTopicDefinitionException;
import com.dang.commonlib.messaging.enums.HeaderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class for sending messages
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageBus {
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;
    private final Environment environment;

    /**
     * Sends a message with headers. Topic is configured with {@link #getTopic(SpecificRecord)} from properties file.
     * To configure a topic for the message, add following property to your application.properties:<br>
     * com.dang.commonlib.messaging.[full-class-name-of-the-message].topic = [topic-value]<br>
     * Example: com.dang.commonlib.messaging.avrogenerated.accountmanager.AccountCreated.topic=accountmanager-event-topic
     * @param record message to be sent
     * @param headerMap map of the header with its values
     */
    public void sendMessage(SpecificRecord record, Map<HeaderEnum, String> headerMap) {
        log.info("Sending message: {}", record);

        List<RecordHeader> headers = headerMap
                .entrySet()
                .stream()
                .map(h -> new RecordHeader(h.getKey().getCode(), h.getValue().getBytes()))
                .toList();

        ProducerRecord<String, SpecificRecord> producerRecord =
                new ProducerRecord<>(
                        getTopic(record),
                        record
                );

        for (Header header : headers) {
            producerRecord.headers().add(header);
        }

        kafkaTemplate.send(producerRecord);
        log.info("Message sent: {}", producerRecord);
    }

    private String getTopic(SpecificRecord record) {
        String topicProperty = this
                .getClass()
                .getPackageName()
                .concat(".")
                .concat(record.getClass().getCanonicalName())
                .concat(".topic");
        String topic = environment.getProperty(topicProperty);
        if (topic == null) {
            throw new MissingTopicDefinitionException("Missing or null property " + topicProperty);
        }
        return topic;
    }
}