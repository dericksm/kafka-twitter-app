package com.github.dericksm.kafka;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaIntroApplication {


    public static void main(String[] args) {
        var app = new SpringApplication(KafkaIntroApplication.class);
        app.run(args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
        return args -> {
            kafkaTemplate.send("derick", "github.com/dericksm");
        };
    }


}
