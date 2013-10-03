package com.arcasolutions.api.deserializer;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleTimeDeserializer extends JsonDeserializer<Date> {

    private static final String tag = "SimpleTimeDeserializer";

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String val = parser.getValueAsString();
        try {
            return TIME_FORMAT.parse(val);
        } catch (Exception e) {
            Log.e(tag, "Parser date error", e);
            return null;
        }
    }
}
