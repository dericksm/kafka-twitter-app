package com.github.dericksm.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {


    @KafkaListener(
            topics = "derick",
            groupId = "groupId"
    )
    void listener(String data){
        System.out.println("Received message: " + data);
    }
}
