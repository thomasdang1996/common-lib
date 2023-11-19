package com.dang.commonlib.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class for ignoring {@link org.apache.avro.Schema} and {@link org.apache.avro.specific.SpecificData}
 * which causes an error when converting object string value to Avro objects
 */
public abstract class IgnoreAvroMixIn {
    @JsonIgnore
    public abstract org.apache.avro.Schema getSchema();

    @JsonIgnore
    public abstract org.apache.avro.specific.SpecificData getSpecificData();
}
