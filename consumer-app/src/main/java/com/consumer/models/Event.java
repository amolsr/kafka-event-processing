package com.consumer.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Event {

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("app_version")
    private long appVersion;

    @JsonProperty("event_info.key")
    private String[] eventInfoKey;

    @JsonProperty("event_info.value")
    private String[] eventInfoValue;

    @JsonProperty("source")
    private String source;

    @JsonProperty("event_category")
    private String eventCategory;

    @JsonProperty("event_status")
    private String eventStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updatedAt")
    private Timestamp updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdAt")
    private Timestamp createdAt;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("uuid")
    private String uuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdAt")
    public void setCreatedAt(Timestamp createdAt) {
        if (createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        } else {
            // LocalDateTime localDateTime = createdAt.toLocalDateTime();
            // localDateTime = localDateTime.minusHours(5).minusMinutes(30);
            // this.createdAt = Timestamp.valueOf(localDateTime);
            this.createdAt = createdAt;
        }
    }

    public String getEventType() {
        return eventType != null ? eventType : "";
    }

    public String getUserType() {
        return userType != null ? userType : "";
    }

    public String getSource() {
        return source != null ? source : "";
    }

    public String getEventCategory() {
        return eventCategory != null ? eventCategory : "";
    }

    public String getDeviceId() {
        return deviceId != null ? deviceId : "";
    }

    public String getIp() {
        return ip != null ? ip : "";
    }

    public String getUuid() {
        return uuid != null ? uuid : "";
    }

    public String getEventStatus() {
        return eventStatus != null ? eventStatus : "null";
    }

    public String[] getEventInfoKey() {
        return eventInfoKey != null ? eventInfoKey : new String[0];
    }

    public String[] getEventInfoValue() {
        return eventInfoValue != null ? eventInfoValue : new String[0];
    }

}
