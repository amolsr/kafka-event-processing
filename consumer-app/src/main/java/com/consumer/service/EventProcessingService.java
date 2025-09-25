package com.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.consumer.dao.EventDAO;
import com.consumer.dto.EventDTO;
import com.consumer.models.Event;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventProcessingService {

    @Autowired
    private EventDAO eventDAO;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.event-log-dlt-topic-id}")
    private String eventDLT;

    public EventProcessingService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processMessages(String[] messages) {
        try {
            List<Event> events = transformMessages(messages);
            eventDAO.batchInsert(events);
            log.info(events.size() + " records inserted into the Clickhouse");
        } catch (SQLException se) {
            log.error("SQL Error while processing batch: ", se);
            sendToDLT(messages);
        } catch (Exception e) {
            log.error("Error while processing batch: ", e);
            sendToDLT(messages);
        }
    }

    public void processMessage(String message) {
        try {
            EventDTO eventDTO = objectMapper.readValue(message, EventDTO.class);
            Event event = transformEvent(eventDTO);
            List<Event> el = new ArrayList<>();
            el.add(event);
            eventDAO.batchInsert(el);
        } catch (SQLException se) {
            log.error("SQL Error while processing batch: ", se);
            // kafkaTemplate.send(eventDLT, message);
        } catch (Exception e) {
            log.error("Error while processing batch: ", e);
            log.error(message);
            // kafkaTemplate.send(eventDLT, message);
        }
    }

    private List<Event> transformMessages(String[] messages) throws Exception {
        List<Event> events = new ArrayList<>();
        for (String message : messages) {
            EventDTO eventDTO = objectMapper.readValue(message, EventDTO.class);
            events.add(transformEvent(eventDTO));
        }
        return events;
    }

    private Event transformEvent(EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        if (eventDTO.getEventInfo() != null) {
            List<String> eventInfoKey = new ArrayList<>(eventDTO.getEventInfo().keySet());
            List<Object> eventInfoValue = new ArrayList<>(eventDTO.getEventInfo().values());

            String[] eventInfoKeyArray = eventInfoKey.toArray(new String[0]);
            String[] eventInfoValueArray = new String[eventInfoValue.size()];

            for (int i = 0; i < eventInfoValue.size(); i++) {
                if (eventInfoValue.get(i) instanceof String[]) {
                    String[] array = (String[]) eventInfoValue.get(i);
                    eventInfoValueArray[i] = String.join(",", array);
                } else {
                    eventInfoValueArray[i] = String.valueOf(eventInfoValue.get(i));
                }
            }
            event.setEventInfoKey(eventInfoKeyArray);
            event.setEventInfoValue(eventInfoValueArray);
        }
        return event;
    }

    private void sendToDLT(String[] messages) {
        for (String message : messages) {
            kafkaTemplate.send(eventDLT, message);
        }
    }
}
