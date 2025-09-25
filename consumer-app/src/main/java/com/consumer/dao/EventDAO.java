package com.consumer.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Connection;
import com.consumer.models.Event;

@Service
public class EventDAO {
    private final DataSource dataSource;

    @Value("${spring.kafka.event-log-table-name}")
    private String eventLogsTableName;

    @Autowired
    public EventDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void batchInsert(List<Event> events) throws SQLException {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO `default`." + eventLogsTableName
                                + " (id, user_type, user_id, device_id, event_category, event_type, event_status, `event_info.key`, `event_info.value`, `source`, ip, createdAt, updatedAt, app_version, uuid) VALUES(?, ?, ? ,? ,?, ?, ?, ? ,? ,?, ?, ?, ? ,? ,?)")) {
            for (int i = 0; i < events.size(); i++) {
                pstmt.setInt(1, 1);
                pstmt.setString(2, events.get(i).getUserType());
                pstmt.setLong(3, events.get(i).getUserId());
                pstmt.setString(4, events.get(i).getDeviceId());
                pstmt.setString(5, events.get(i).getEventCategory());
                pstmt.setString(6, events.get(i).getEventType());
                pstmt.setString(7, events.get(i).getEventStatus());
                Array eventInfoKey = conn.createArrayOf("String", events.get(i).getEventInfoKey());
                pstmt.setArray(8, eventInfoKey);
                Array eventInfoValue = conn.createArrayOf("String", events.get(i).getEventInfoValue());
                pstmt.setArray(9, eventInfoValue);
                pstmt.setString(10, events.get(i).getSource());
                pstmt.setString(11, events.get(i).getIp());
                pstmt.setTimestamp(12, events.get(i).getCreatedAt());
                pstmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
                pstmt.setLong(14, events.get(i).getAppVersion());
                pstmt.setString(15, events.get(i).getUuid());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
