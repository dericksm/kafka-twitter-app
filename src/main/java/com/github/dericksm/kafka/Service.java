package com.github.dericksm.kafka;

import com.github.dericksm.kafka.twiiterproducer.TweetStreamListenerExecutor;
import com.github.dericksm.kafka.twiiterproducer.TweetListenerImpl;
import com.github.dericksm.kafka.twiiterproducer.TweetsStreamListener;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.StreamingTweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class Service {

    private final TwitterApi twitterApi;
    private final KafkaTemplate kakfaTemplate;
    final Queue<StreamingTweet> queue = new LinkedList<>();
    private final List<TweetsStreamListener> listeners = new ArrayList<>();
    private volatile boolean isRunning = true;

    public Service(TwitterApi twitterApi, KafkaTemplate kakfaTemplate) {
        this.twitterApi = twitterApi;
        this.kakfaTemplate = kakfaTemplate;
    }


    @Scheduled(fixedDelay = 10000)
    private void callApi() {
        Set<String> tweetFields = new HashSet<>();
        tweetFields.add("author_id");
        tweetFields.add("id");
        tweetFields.add("created_at");

        try {
            InputStream streamResult = twitterApi.tweets().sampleStream(null, tweetFields, null, null, null, null, 0);
            TweetsStreamListener responder = new TweetListenerImpl();
            TweetStreamListenerExecutor tsle = new TweetStreamListenerExecutor(kakfaTemplate, streamResult);
            tsle.addListener(responder);
            tsle.executeListeners();

        } catch (ApiException e) {
            log.error("Status code: " + e.getCode());
            log.error("Reason: " + e.getResponseBody());
            log.error("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
