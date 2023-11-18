package com.dang.commonlib.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IgnoreAvroMixIn {
    @JsonIgnore
    public abstract org.apache.avro.Schema getSchema();

    @JsonIgnore
    public abstract org.apache.avro.specific.SpecificData getSpecificData();
}
