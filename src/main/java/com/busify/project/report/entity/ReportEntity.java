package com.busify.project.report.entity;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportEntity {
    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.UUID)
    private String id;
    private String title;
    private Instant reportDate;
    private Long operatorId;
    private Map<String, Object> data;
}