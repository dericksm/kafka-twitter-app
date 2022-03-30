package com.github.dericksm.kafka.config.twitter;

import com.twitter.clientlib.ApiClient;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.TwitterCredentialsOAuth1;
import com.twitter.clientlib.api.TwitterApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterApiConfig {

    @Value(value = "${twitter.oauth.consumerKey}")
    private String twitterConsumerKey;
    @Value(value = "${twitter.oauth.consumerSecret}")
    private String twitterConsumerSecret;
    @Value(value = "${twitter.oauth.accessToken}")
    private String twitterAccessToken;
    @Value(value = "${twitter.oauth.accessTokenSecret}")
    private String twitterAccessTokenSecret;
    @Value(value = "${twitter.oauth.bearer}")
    private String twitterBearer;

    @Bean
    public TwitterApi twitterApi() throws ApiException {
        TwitterApi api = new TwitterApi(getApiClient());
        api.setTwitterCredentials(new TwitterCredentialsBearer(twitterBearer));
        return api;
    }

    private TwitterCredentialsOAuth1 getTwitterCredentials() {
        var twitterCredentials = new TwitterCredentialsOAuth1(null, null, null, null);
        twitterCredentials.setTwitterConsumerKey(twitterConsumerKey);
        twitterCredentials.setTwitterConsumerSecret(twitterConsumerSecret);
        twitterCredentials.setTwitterToken(twitterAccessToken);
        twitterCredentials.setTwitterTokenSecret(twitterAccessTokenSecret);
        return twitterCredentials;
    }

    private ApiClient getApiClient() {
        return new ApiClient();
    }
}


//        String query = "bitcoin OR BTC OR Bitcoin";
////        Set tweetFields = Collections.singleton("geo");
//        Set tweetFields = new HashSet();
//        tweetFields.add("geo");
//        tweetFields.add("author_id");
//
//        TweetSearchResponse result = api.tweetsRecentSearch(query, OffsetDateTime.now().minusHours(1), null, null,
//                null, null, null, Collections.singleton("geo.place_id"), tweetFields,
//                Collections.singleton("location"), null, Collections.singleton("geo"), null);
//
//        List<TweetGeo> geoList = new ArrayList<>();
//        String nextToken = null;
//        TweetSearchResponse response = null;
//        do {
//            response = api.tweetsRecentSearch("bitcoin OR BTC OR Bitcoin", OffsetDateTime.now().minusWeeks(1), null, null,
//                    null, 100, nextToken != null ? nextToken : null, Collections.singleton("geo.place_id"), tweetFields,
//                    Collections.singleton("location"), null, Collections.singleton("geo"), null);
//            nextToken = response.getMeta().getNextToken();
//            geoList.addAll(result.getData().stream().map(Tweet::getGeo).filter(Objects::nonNull).collect(Collectors.toList()));
//        } while (geoList.size() < 10);
//        System.out.println("saiu dessa merda");
