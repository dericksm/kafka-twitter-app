package com.github.dericksm.kafka.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.clientlib.model.Tweet;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class TweetDeserializer implements Deserializer {
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public void configure(Map configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null) {
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(bytes, "UTF-8"), Tweet.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
