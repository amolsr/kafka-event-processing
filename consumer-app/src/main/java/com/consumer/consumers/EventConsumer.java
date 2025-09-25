package com.consumer.consumers;

import com.consumer.service.EventProcessingService;
import com.consumer.service.EmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventConsumer {

    private final EventProcessingService eventProcessingService;

    private final EmailService emailService;

    @Value("${spring.kafka.event-log-dlt-topic-id}")
    private String eventDLT;

    public EventConsumer(EventProcessingService eventProcessingService, EmailService emailService) {
        this.eventProcessingService = eventProcessingService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${spring.kafka.event-log-topic-id}", groupId = "${spring.kafka.event-log-consumer-group-id}", containerFactory = "kafkaBatchListenerContainerFactory", concurrency = "${spring.kafka.event-log-concurrency}")
    public void consume(String[] messages) {
        log.info(messages.length + " records to be inserted into the Clickhouse");
        eventProcessingService.processMessages(messages);
    }

    @KafkaListener(topics = "${spring.kafka.event-log-topic-id}", groupId = "${spring.kafka.email-consumer-group-id}", containerFactory = "kafkaBatchListenerContainerFactory", concurrency = "${spring.kafka.event-log-concurrency}")
    public void consumeForEmail(String[] messages) {
        emailService.processMessagesForEmail(messages);
    }
}
