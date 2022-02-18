package com.github.dericksm.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemo.class);
    private static final String SERVER = "localhost:9092";

    public static void main(String[] args) {
        ProducerRecord<String, String> record = null;
        KafkaProducer producer = getProducer();

        for (int i = 0; i < 10; i++) {
            record = new ProducerRecord<>("first_kafka_topic", "id_" + i, "hello");
            producer.send(record, new KafkaCallback());
            producer.flush();
        }

    }

    private static KafkaProducer getProducer() {
        return new KafkaProducer<String, String>(getKafkaProperties());
    }


    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    static class KafkaCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e == null) {
                LOGGER.info("Successfully produced");
                LOGGER.info("Partition: " + recordMetadata.partition());
            } else {
                LOGGER.error("Error while producing", e);
            }
        }
    }
}
