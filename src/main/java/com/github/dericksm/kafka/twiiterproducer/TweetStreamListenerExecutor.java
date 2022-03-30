package com.github.dericksm.kafka.twiiterproducer;

import com.google.gson.reflect.TypeToken;
import com.twitter.clientlib.JSON;
import com.twitter.clientlib.model.SingleTweetLookupResponse;
import com.twitter.clientlib.model.StreamingTweet;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Source
 * https://github.dev/twitterdev/twitter-api-java-sdk/blob/main/examples/src/main/java/com/twitter/clientlib/TweetsStreamListenersExecutor.java
 */
public class TweetStreamListenerExecutor {
    private final KafkaTemplate kakfaTemplate;
    private final ITweetsQueue tweetsQueue;
    private final List<TweetsStreamListener> listeners = new ArrayList<>();
    private final InputStream stream;
    private volatile boolean isRunning = true;

    public TweetStreamListenerExecutor(KafkaTemplate kakfaTemplate, InputStream stream) {
        this.kakfaTemplate = kakfaTemplate;
        this.tweetsQueue = new LinkedListTweetsQueue();
        this.stream = stream;
    }

    public void addListener(TweetsStreamListener toAdd) {
        listeners.add(toAdd);
    }

    public void executeListeners() {
        if (stream == null) {
            System.out.println("Error: stream is null.");
            return;
        } else if (this.tweetsQueue == null) {
            System.out.println("Error: tweetsQueue is null.");
            return;
        }

        TweetsQueuer tweetsQueuer = new TweetsQueuer();
        TweetsListenersExecutor tweetsListenersExecutor = new TweetsListenersExecutor();
        tweetsListenersExecutor.start();
        tweetsQueuer.start();
    }

    public synchronized void shutdown() {
        isRunning = false;
        System.out.println("TweetsStreamListenersExecutor is shutting down.");
    }

    private class TweetsListenersExecutor extends Thread {
        @Override
        public void run() {
            processTweets();
        }

        private void processTweets() {
            StreamingTweet streamingTweet;
            try {
                while (isRunning) {
                    streamingTweet = tweetsQueue.poll();
                    if (streamingTweet == null) {
                        Thread.sleep(10000);
                        continue;
                    }
                    for (TweetsStreamListener listener : listeners) {
                        listener.actionOnTweetsStream(streamingTweet, kakfaTemplate);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TweetsQueuer extends Thread {
        @Override
        public void run() {
            queueTweets();
        }

        public void queueTweets() {
            JSON json = new JSON();
            Type localVarReturnType = new TypeToken<SingleTweetLookupResponse>() {
            }.getType();

            String line = null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                while (isRunning) {
                    line = reader.readLine();
                    if (line == null || line.isEmpty()) {
                        Thread.sleep(10000);
                        continue;
                    }
                    try {
                        tweetsQueue.add(StreamingTweet.fromJson(line));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                shutdown();
            }
        }
    }
}

interface ITweetsQueue {
    StreamingTweet poll();
    void add(StreamingTweet streamingTweet);
}

class LinkedListTweetsQueue implements ITweetsQueue {
    private final Queue<StreamingTweet> tweetsQueue = new LinkedList<>();

    @Override
    public StreamingTweet poll() {
        return tweetsQueue.poll();
    }

    @Override
    public void add(StreamingTweet streamingTweet) {
        tweetsQueue.add(streamingTweet);
    }
}
