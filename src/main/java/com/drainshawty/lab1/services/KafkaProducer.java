package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.EmailData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaProducer {
    KafkaTemplate<String, EmailData> kafkaTemplate;

    public void sendMessage(EmailData data) {
        kafkaTemplate.send("mailer", data);
    }
}
