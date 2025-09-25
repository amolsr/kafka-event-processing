package com.consumer.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class EventInfoSerializer extends JsonSerializer<Map<String, Object>> {

    @Override
    public void serialize(Map<String, Object> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val == null) {
                gen.writeNullField(key);
            } else if (val instanceof String) {
                gen.writeStringField(key, (String) val);
            } else if (val instanceof Number) {
                gen.writeNumberField(key, ((Number) val).doubleValue());
            } else if (val instanceof Boolean) {
                gen.writeBooleanField(key, (Boolean) val);
            } else if (val instanceof String[]) {
                gen.writeArrayFieldStart(key);
                for (String str : (String[]) val) {
                    gen.writeString(str);
                }
                gen.writeEndArray();
            } else {
                gen.writeStringField(key, val.toString());
            }
        }

        gen.writeEndObject();
    }
}
