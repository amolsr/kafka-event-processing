package com.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("app_version")
    private Long appVersion;

    @JsonDeserialize(using = EventInfoDeserializer.class)
    @JsonSerialize(using = EventInfoSerializer.class)
    @JsonProperty("event_info")
    private Map<String, Object> eventInfo;

    @JsonProperty("source")
    private String source;

    @JsonProperty("event_category")
    private String eventCategory;

    @JsonProperty("event_status")
    private String eventStatus;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty("updatedAt")
    private Date updatedAt;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("uuid")
    private String uuid;
}