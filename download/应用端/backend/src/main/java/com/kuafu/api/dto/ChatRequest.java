package com.kuafu.api.dto;

import com.kuafu.llm.model.ChatMessage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Request body for chat conversation.
 */
@Data
public class ChatRequest {
    @NotNull(message = "query不能为空")
    private String query;

    private String conversationId;

    /** recent history sent from client */
    private List<ChatMessage> messages;
}