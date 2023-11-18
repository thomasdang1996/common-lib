package com.dang.commonlib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringUtils {


    private final ObjectMapper objectMapper;

    public String toString(Object object) {
        try {
            objectMapper.addMixIn(
                    SpecificRecord.class,
                    IgnoreAvroMixIn.class
            );
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(String objectString, Class<T> classType) {
        try {
            objectMapper.addMixIn(
                    SpecificRecord.class,
                    IgnoreAvroMixIn.class
            );
            return objectMapper.readValue(objectString, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
