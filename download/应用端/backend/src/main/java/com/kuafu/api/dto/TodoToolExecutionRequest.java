package com.kuafu.api.dto;

import lombok.Data;

@Data
public class TodoToolExecutionRequest {
    private String conversationId;
    private Long actionLogId;
}