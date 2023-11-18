//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dang.commonlib.utils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.apache.avro.AvroMissingFieldException;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Conversion;
import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.data.RecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.AvroGenerated;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.avro.specific.SpecificRecordBuilderBase;

@AvroGenerated
public class AccountCreated extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = -6179355059984820996L;
    public static final Schema SCHEMA$ = (new Schema.Parser()).parse("{\"type\":\"record\",\"name\":\"AccountCreated\",\"namespace\":\"avrogenerated.accountmanager\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"username\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
    private static final SpecificData MODEL$ = new SpecificData();
    private static final BinaryMessageEncoder<AccountCreated> ENCODER;
    private static final BinaryMessageDecoder<AccountCreated> DECODER;
    private UUID id;
    private String username;
    private static final Conversion<?>[] conversions;
    private static final DatumWriter<AccountCreated> WRITER$;
    private static final DatumReader<AccountCreated> READER$;

    public static Schema getClassSchema() {
        return SCHEMA$;
    }

    public static BinaryMessageEncoder<AccountCreated> getEncoder() {
        return ENCODER;
    }

    public static BinaryMessageDecoder<AccountCreated> getDecoder() {
        return DECODER;
    }

    public static BinaryMessageDecoder<AccountCreated> createDecoder(SchemaStore resolver) {
        return new BinaryMessageDecoder(MODEL$, SCHEMA$, resolver);
    }

    public ByteBuffer toByteBuffer() throws IOException {
        return ENCODER.encode(this);
    }

    public static AccountCreated fromByteBuffer(ByteBuffer b) throws IOException {
        return (AccountCreated)DECODER.decode(b);
    }

    public AccountCreated() {
    }

    public AccountCreated(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public SpecificData getSpecificData() {
        return MODEL$;
    }

    public Schema getSchema() {
        return SCHEMA$;
    }

    public Object get(int field$) {
        switch (field$) {
            case 0:
                return this.id;
            case 1:
                return this.username;
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + field$);
        }
    }

    public Conversion<?> getConversion(int field) {
        return conversions[field];
    }

    public void put(int field$, Object value$) {
        switch (field$) {
            case 0:
                this.id = (UUID)value$;
                break;
            case 1:
                this.username = value$ != null ? value$.toString() : null;
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + field$);
        }

    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID value) {
        this.id = value;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Builder other) {
        return other == null ? new Builder() : new Builder(other);
    }

    public static Builder newBuilder(AccountCreated other) {
        return other == null ? new Builder() : new Builder(other);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        WRITER$.write(this, SpecificData.getEncoder(out));
    }

    public void readExternal(ObjectInput in) throws IOException {
        READER$.read(this, SpecificData.getDecoder(in));
    }

    static {
        MODEL$.addLogicalTypeConversion(new Conversions.UUIDConversion());
        ENCODER = new BinaryMessageEncoder(MODEL$, SCHEMA$);
        DECODER = new BinaryMessageDecoder(MODEL$, SCHEMA$);
        conversions = new Conversion[]{new Conversions.UUIDConversion(), null, null};
        WRITER$ = MODEL$.createDatumWriter(SCHEMA$);
        READER$ = MODEL$.createDatumReader(SCHEMA$);
    }

    @AvroGenerated
    public static class Builder extends SpecificRecordBuilderBase<AccountCreated> implements RecordBuilder<AccountCreated> {
        private UUID id;
        private String username;

        private Builder() {
            super(AccountCreated.SCHEMA$, AccountCreated.MODEL$);
        }

        private Builder(Builder other) {
            super(other);
            if (isValidValue(this.fields()[0], other.id)) {
                this.id = (UUID)this.data().deepCopy(this.fields()[0].schema(), other.id);
                this.fieldSetFlags()[0] = other.fieldSetFlags()[0];
            }

            if (isValidValue(this.fields()[1], other.username)) {
                this.username = (String)this.data().deepCopy(this.fields()[1].schema(), other.username);
                this.fieldSetFlags()[1] = other.fieldSetFlags()[1];
            }

        }

        private Builder(AccountCreated other) {
            super(AccountCreated.SCHEMA$, AccountCreated.MODEL$);
            if (isValidValue(this.fields()[0], other.id)) {
                this.id = (UUID)this.data().deepCopy(this.fields()[0].schema(), other.id);
                this.fieldSetFlags()[0] = true;
            }

            if (isValidValue(this.fields()[1], other.username)) {
                this.username = (String)this.data().deepCopy(this.fields()[1].schema(), other.username);
                this.fieldSetFlags()[1] = true;
            }

        }

        public UUID getId() {
            return this.id;
        }

        public Builder setId(UUID value) {
            this.validate(this.fields()[0], value);
            this.id = value;
            this.fieldSetFlags()[0] = true;
            return this;
        }

        public boolean hasId() {
            return this.fieldSetFlags()[0];
        }

        public Builder clearId() {
            this.id = null;
            this.fieldSetFlags()[0] = false;
            return this;
        }

        public String getUsername() {
            return this.username;
        }

        public Builder setUsername(String value) {
            this.validate(this.fields()[1], value);
            this.username = value;
            this.fieldSetFlags()[1] = true;
            return this;
        }

        public boolean hasUsername() {
            return this.fieldSetFlags()[1];
        }

        public Builder clearUsername() {
            this.username = null;
            this.fieldSetFlags()[1] = false;
            return this;
        }

        public AccountCreated build() {
            try {
                AccountCreated record = new AccountCreated();
                record.id = this.fieldSetFlags()[0] ? this.id : (UUID)this.defaultValue(this.fields()[0]);
                record.username = this.fieldSetFlags()[1] ? this.username : (String)this.defaultValue(this.fields()[1]);
                return record;
            } catch (AvroMissingFieldException var2) {
                throw var2;
            } catch (Exception var3) {
                throw new AvroRuntimeException(var3);
            }
        }
    }
}
