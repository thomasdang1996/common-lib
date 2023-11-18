package com.dang.commonlib.messaging;

import com.dang.commonlib.exception.MissingTopicDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageBus {
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;
    private final Environment environment;

    public MessageBus(KafkaTemplate<String, SpecificRecord> kafkaTemplate, Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
    }

    public void sendMessage(SpecificRecord record, List<Header> headers) {
        log.info("Sending message: {}", record);

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