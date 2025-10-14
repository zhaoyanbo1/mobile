package com.kuafu.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoToolPayloadView {
    private Integer reminderItemId;
    private String type;
    private String title;
    private String dueAt;
    private String priority;
    private String notes;
    private String dosage;
}