package com.dang.commonlib.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.specific.SpecificRecord;
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
        SpecificRecord obj = utils.toObject(
                str,
                "com.dang.commonlib.utils.AccountCreated");
        assertThat(obj).isInstanceOf(AccountCreated.class);
    }
}
