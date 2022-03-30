# Twiter with Kafka Streams

The project has a Kafka Producer that publishes twitter data.  
We also have the Storm with a topology that uses a KakfaSpout to consume the twitter data and apply some logic on it.  
The spout is configured with a WordCounterBold that is not done yet.
