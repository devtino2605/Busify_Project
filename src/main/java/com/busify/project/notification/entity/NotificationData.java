package com.busify.project.notification.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notifications")
public class NotificationData {
    @Id
    private Long id;
    private String message;
    private String title;
    private Map<String, Object> data;
    private String sub;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("message", message);
        map.put("title", title);
        map.put("data", data);
        return map;
    }
}
