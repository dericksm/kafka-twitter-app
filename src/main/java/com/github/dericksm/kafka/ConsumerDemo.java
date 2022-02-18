package com.github.dericksm.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemo.class);
    private static final String SERVER = "localhost:9092";

    public static void main(String[] args) {
        KafkaConsumer consumer = getConsumer();
        consumer.subscribe(Collections.singleton("first_kafka_topic"));
        while(true){
            ConsumerRecords<String , String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                LOGGER.info("Key: " + record.key());
                LOGGER.info("Value: " + record.value());
                LOGGER.info("Partition: " + record.partition());
            }
        }

    }

    private static KafkaConsumer getConsumer() {
        return new KafkaConsumer<String, String>(getKafkaProperties());
    }


    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "dericks_group");
        return properties;
    }
}
