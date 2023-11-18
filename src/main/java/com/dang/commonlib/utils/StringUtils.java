package com.dang.commonlib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

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

    public SpecificRecord toObject(String objectString, String fullClassName) {
        try {
            return objectMapper.readValue(objectString, getTypeReference(fullClassName));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get type reference by the className
     * @param fullClassName
     * @return
     */
    private TypeReference<? extends SpecificRecord> getTypeReference(String fullClassName) {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return getClassForName(fullClassName);
            }
        };
    }

    private Class<?> getClassForName(String classTypeName) {
        try {
            return Class.forName(classTypeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
