package com.dang.commonlib.messaging;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageUtils {
    public static Map<String, String> toHeaderMap(Headers headers) {
        return Arrays
                .stream(headers.toArray())
                .collect(
                        Collectors.toMap(
                                Header::key,
                                header -> new String(header.value())
                        )
                );
    }
}
