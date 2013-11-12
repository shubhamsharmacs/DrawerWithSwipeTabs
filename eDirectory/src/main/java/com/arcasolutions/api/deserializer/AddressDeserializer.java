package com.arcasolutions.api.deserializer;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class AddressDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String address = parser.getValueAsString();
        if (TextUtils.isEmpty(address)) return address;
        return address.replaceAll("(,\\s){2,}", ", ");
    }
}
