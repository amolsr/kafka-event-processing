package com.consumer.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.consumer.models.EmailRequest;
import com.consumer.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailConsumer {

    private final EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.email-dlt-topic-id}")
    private String emailDLT;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${spring.kafka.email-topic-id}", groupId = "${spring.kafka.email-consumer-group-id}", containerFactory = "kafkaListenerContainerFactory", concurrency = "${spring.kafka.email-concurrency}")
    public void listen(String message) {
        try {
            EmailRequest request = objectMapper.readValue(message, EmailRequest.class);
            emailService.sendMessageToEndpoint(request);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
