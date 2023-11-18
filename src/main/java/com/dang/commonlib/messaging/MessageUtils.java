package com.dang.commonlib.messaging;

import com.dang.commonlib.messaging.enums.HeaderEnum;
import org.apache.kafka.common.header.Headers;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageUtils {
    public static Map<HeaderEnum, String> toHeaderMap(Headers headers) {
        return Arrays
                .stream(headers.toArray())
                .collect(
                        Collectors.toMap(
                                header -> HeaderEnum.getHeaderByCode(header.key()),
                                header -> new String(header.value())
                        )
                );
    }
}
