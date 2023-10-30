package com.dang.commonlib.messaging;

import com.dang.commonlib.exception.MissingTopicDefinitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageBus {
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;
    private final Environment environment;
    public void sendMessage(SpecificRecord record) {
        try {
            log.info("Sending message: {}", record);
            String topic = getTopic(record);
            SendResult<String, SpecificRecord> result = kafkaTemplate
                    .send(topic, record)
                    .get();

            ProducerRecord<String, SpecificRecord> producerRecord = result.getProducerRecord();
            log.info("Message sent: {}", producerRecord);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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