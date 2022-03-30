package com.github.dericksm.kafka.twiiterproducer;

import com.twitter.clientlib.model.StreamingTweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class TweetListenerImpl implements TweetsStreamListener {
    @Override
    public void actionOnTweetsStream(StreamingTweet streamingTweet, KafkaTemplate kafkaTemplate) {
        if (streamingTweet == null) {
            log.error("Error: actionOnTweetsStream - streamingTweet is null ");
            return;
        }

        if (streamingTweet.getErrors() != null) {
            streamingTweet.getErrors().forEach(System.out::println);
        } else if (streamingTweet.getData() != null) {
           kafkaTemplate.send("tweets-topic", streamingTweet.getData());
        }
    }
}
