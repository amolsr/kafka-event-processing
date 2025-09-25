package com.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.consumer.constants.EmailConstants;
import com.consumer.dto.EventDTO;
import com.consumer.models.EmailRequest;
import com.consumer.models.EmailRequest.CommonFrom;
import com.consumer.models.EmailRequest.RecipientGroup;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Service
@Slf4j
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${spring.kafka.email-base-url}")
    private String baseUrl;

    @Value("${spring.kafka.email-endpoint}")
    private String endpoint;

    @Value("${spring.kafka.email-topic-id}")
    private String emailTopic;

    @Value("${spring.kafka.email-sender-email}")
    private String emailSenderEmail;

    @Value("${spring.kafka.email-sender-name}")
    private String emailSenderName;

    @Autowired
    private ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EmailService(RestTemplate restTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToEndpoint(EmailRequest request) {
        String url = baseUrl + endpoint;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("SystemId", "11");
        headers.set("AppId", "152");

        // Create the request entity with body and headers
        HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            // Print the full response, including headers
            System.out.println("API Response: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

        } catch (Exception e) {
            System.err.println("API Error: " + e.getMessage());
        }
    }

    private List<EventDTO> transformMessages(String[] messages) throws Exception {
        List<EventDTO> events = new ArrayList<>();
        for (String message : messages) {
            EventDTO eventDTO = objectMapper.readValue(message, EventDTO.class);
            events.add(eventDTO);
        }
        return events;
    }

    public void processMessagesForEmail(String[] messages) {
        try {
            List<EventDTO> events = transformMessages(messages);

            // Filter events based on criteria
            List<EventDTO> emailEvents = filterEvents(events);
            publishEmailTopic(emailEvents);

            log.info(emailEvents.size() + " events pushed to Email");
        } catch (Exception e) {
            log.error("Error while processing batch: ", e);
        }
    }

    // Method to filter events based on criteria
    private List<EventDTO> filterEvents(List<EventDTO> events) {
        List<EventDTO> filteredEvents = new ArrayList<>();
        for (EventDTO event : events) {
            if (event.getEventType() != null && EmailConstants.EMAIL_EVENTS.contains(event.getEventType())) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    // Method to publish filtered events to Kafka
    private void publishEmailTopic(List<EventDTO> filteredEvents) {
        for (EventDTO event : filteredEvents) {
            try {
                // Map the event to an Email request
                if (event.getEventInfo().get("email") != null) {
                    EmailRequest emailRequest = mapEventToEmailRequest(event);
                    String eventJson = objectMapper.writeValueAsString(emailRequest);
                    kafkaTemplate.send(emailTopic, eventJson);
                }
            } catch (Exception e) {
                log.error("Error while publishing filtered event: ", e);
            }
        }
    }

    private EmailRequest mapEventToEmailRequest(EventDTO event) {
        try {
            EmailRequest emailRequest = new EmailRequest();

            // Create Sender
            EmailRequest.Sender sender = createSender(emailSenderEmail, emailSenderName);

            if (event.getEventInfo().get("name") == null) {
                event.getEventInfo().put("name", "");
            }

            // Create Recipient
            EmailRequest.Recipient recipient = createRecipient(
                    event.getEventInfo().get("email").toString(),
                    event.getEventInfo().get("name").toString(),
                    String.valueOf(event.getUserId()));

            // Create Recipient Group
            RecipientGroup recipientGroup = createRecipientGroup(sender, recipient, event.getEventInfo());

            // Set EmailRequest fields
            emailRequest.setTo(Arrays.asList(recipientGroup));
            emailRequest.setCommonSender(sender);
            emailRequest.setCommonFrom(createCommonFrom(sender));
            emailRequest.setTemplateBody("");
            emailRequest.setSubject("");
            emailRequest.setEventCode("jobseeker_" + event.getEventType().toLowerCase() + "_mail");

            log.debug("Mapped EmailRequest: {}", emailRequest);
            return emailRequest;
        } catch (Exception e) {
            log.info("Error in event: {}", event.getEventType());
            log.info("payload: {}", event.getEventInfo());
            throw e;
        }
    }

    private EmailRequest.Sender createSender(String id, String name) {
        EmailRequest.Sender sender = new EmailRequest.Sender();
        sender.setId(id);
        sender.setName(name);
        return sender;
    }

    private EmailRequest.Recipient createRecipient(String id, String name, String userId) {
        EmailRequest.Recipient recipient = new EmailRequest.Recipient();
        recipient.setId(id);
        recipient.setName(name);
        recipient.setUserId(userId);
        return recipient;
    }

    private RecipientGroup createRecipientGroup(EmailRequest.Sender sender, EmailRequest.Recipient recipient,
            Map<String, Object> buildData) {
        RecipientGroup recipientGroup = new RecipientGroup();
        recipientGroup.setMedium("mail");
        recipientGroup.setSender(sender);
        recipientGroup.setFrom(sender);
        recipientGroup.setRecipients(Arrays.asList(recipient));
        recipientGroup.setBuildData(buildData);
        return recipientGroup;
    }

    private CommonFrom createCommonFrom(EmailRequest.Sender sender) {
        CommonFrom commonFrom = new CommonFrom();
        commonFrom.setMail(sender);
        return commonFrom;
    }
}
