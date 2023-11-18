package com.dang.commonlib.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class StringUtilsTest {

    @Test
    void toString_success() {
        ObjectMapper objectMapper = new ObjectMapper();
        StringUtils utils = new StringUtils(objectMapper);
        AccountCreated created = new AccountCreated();
        String str = utils.toString(created);
    }

    @Test
    void toObject_success() {
        ObjectMapper objectMapper = new ObjectMapper();
        StringUtils utils = new StringUtils(objectMapper);
        AccountCreated created = new AccountCreated();
        String str = utils.toString(created);
        Object obj = utils.toObject(str,AccountCreated.class);
        System.out.println(obj);
        assertThat(obj).isInstanceOf(AccountCreated.class);
    }
}
