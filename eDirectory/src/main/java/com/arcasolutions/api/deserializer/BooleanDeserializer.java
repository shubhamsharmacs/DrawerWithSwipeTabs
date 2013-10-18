package com.arcasolutions.api.deserializer;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    private static final String tag = "BooleanDeserializer";

    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String val = parser.getValueAsString();
        try {
            if ("Y".equalsIgnoreCase(val)) return true;
            if ("N".equalsIgnoreCase(val)) return false;
            if ("true".equalsIgnoreCase(val)) return true;
            if ("false".equalsIgnoreCase(val)) return false;
            return false;
        } catch (Exception e) {
            Log.e(tag, "Parser boolean error", e);
            return false;
        }
    }
}
