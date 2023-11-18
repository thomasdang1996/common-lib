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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageBus {
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;
    private final Environment environment;

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

    public void sendMessage(SpecificRecord record, UUID messageId) {
        log.info("Sending message: {}", record);

        ProducerRecord<String, SpecificRecord> producerRecord =
                new ProducerRecord<>(
                        getTopic(record),
                        record
                );

        producerRecord
                .headers()
                .add(
                        new RecordHeader("messageId", messageId.toString().getBytes())
                );

        kafkaTemplate.send(producerRecord);
        log.info("Message sent: {}", producerRecord);
    }


}