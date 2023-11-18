package com.dang.commonlib.messaging.enums;

import lombok.Getter;

@Getter
public enum HeaderEnum {
    MESSAGE_ID("messageId");

    private final String value;
    HeaderEnum(String value) {
        this.value=value;
    }
}
