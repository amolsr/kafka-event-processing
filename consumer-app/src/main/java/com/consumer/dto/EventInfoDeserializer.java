package com.consumer.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventInfoDeserializer extends JsonDeserializer<Map<String, Object>> {

    @Override
    public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        Map<String, Object> result = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            result.put(key, parseJsonNode(value, mapper));
        }

        return result;
    }

    private Object parseJsonNode(JsonNode node, ObjectMapper mapper) {
        if (node == null || node.isNull()) {
            return null;
        } else if (node.isArray()) {
            if (node.size() > 0 && node.get(0).isTextual()) {
                return mapper.convertValue(node, String[].class);
            } else {
                // Handle other array types if needed
                return node.toString();
            }
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isNumber()) {
            return node.numberValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else {
            // Fallback to JSON string representation for complex/nested structures
            return node.toString();
        }
    }
}
