package com.kuafu.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoToolExecutionResult {
    private TodoToolPayloadView todo;
    private String assistantMessage;
}