package com.dang.commonlib.messaging;

import com.dang.commonlib.exception.MissingTopicDefinitionException;
import lombok.Builder;
import lombok.Data;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageBusTest {
    @Mock
    private Environment environment;
    @Mock
    private KafkaTemplate<String, SpecificRecord> kafkaTemplate;
    @InjectMocks
    private MessageBus messageBus;

    @Test
    public void sendMessage_success() {
        String topic = "some-topic-01";
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SpecificRecord> recordCaptor = ArgumentCaptor.forClass(SpecificRecord.class);
        MockMessage message = new MockMessage("name", "email");

        when(environment.getProperty(isA(String.class)))
                .thenReturn(topic);

        when(kafkaTemplate.send(isA(String.class), isA(SpecificRecord.class)))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                new SendResult<>(
                                        new ProducerRecord<>(topic, message),
                                        null
                                )
                        )
                );

        messageBus.sendMessage(message);

        verify(kafkaTemplate)
                .send(stringCaptor.capture(), recordCaptor.capture());

        assertThat(recordCaptor.getValue())
                .isEqualTo(message);
    }

    @Test
    public void sendMessage_failed() {
        MockMessage message = new MockMessage("name", "email");
        when(environment.getProperty(isA(String.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> messageBus.sendMessage(message))
                .isInstanceOf(MissingTopicDefinitionException.class)
                .hasMessage("Missing or null property com.dang.commonlib.messaging.com.dang.commonlib.messaging.MessageBusTest.MockMessage.topic");
    }


    @Data
    @Builder
    public static class MockMessage implements SpecificRecord {
        private String username;
        private String email;

        @Override
        public void put(int i, Object v) {

        }

        @Override
        public Object get(int i) {
            return null;
        }

        @Override
        public Schema getSchema() {
            return null;
        }
    }
}
