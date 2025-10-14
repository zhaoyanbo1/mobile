package com.kuafu.api.dto;

import lombok.Data;

@Data
public class TodoToolDeclineRequest {
    private String conversationId;
    private Long actionLogId;
    private String reason;
}