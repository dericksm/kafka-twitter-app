package com.github.dericksm.kafka.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.clientlib.model.Tweet;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class TweetSerializer implements Serializer<Tweet> {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public byte[] serialize(String s, Tweet tweet) {
        try {
            if (tweet == null) {
                System.out.println("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(tweet);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Tweet to byte[]");
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
