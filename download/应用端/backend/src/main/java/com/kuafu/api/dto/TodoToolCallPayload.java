package com.kuafu.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoToolCallPayload {
    private String type;
    private String title;
    @JsonAlias("due_at")
    private String dueAt;
    private String priority;
    private String notes;
    private String dosage;
}