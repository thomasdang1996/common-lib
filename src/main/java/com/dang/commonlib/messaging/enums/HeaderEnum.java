package com.dang.commonlib.messaging.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum HeaderEnum {
    MESSAGE_ID("messageId");

    private final String code;

    HeaderEnum(String code) {
        this.code = code;
    }

    public static HeaderEnum getHeaderByCode(String code) {
        return Arrays
                .stream(values())
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ProductType not found: " + code));
    }
}
