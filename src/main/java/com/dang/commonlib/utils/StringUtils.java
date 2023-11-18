package com.dang.commonlib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringUtils {


    private final ObjectMapper objectMapper;

    public String toString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object toObject(String objectString, Class<Object> classType) {
        return objectMapper.convertValue(objectString, classType);
    }
}
