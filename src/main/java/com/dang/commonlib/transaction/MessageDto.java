package com.dang.commonlib.transaction;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data

public class MessageDto {
    private UUID messageId;
    private String className;
    private String value;
}
