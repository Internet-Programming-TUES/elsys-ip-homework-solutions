package com.elsys.ip.springboottimerhomework.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SecondsTohhmmssSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer seconds, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeString(String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60));
    }
}