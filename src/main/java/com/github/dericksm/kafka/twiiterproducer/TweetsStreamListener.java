package com.github.dericksm.kafka.twiiterproducer;

import com.twitter.clientlib.model.StreamingTweet;
import org.springframework.kafka.core.KafkaTemplate;

public interface TweetsStreamListener {
    void actionOnTweetsStream(StreamingTweet streamingTweet, KafkaTemplate kafkaTemplate);
}

