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

    /** optional title hint when creating a new conversation */
    private String conversationTitle;

    /** identifies the end user for persistence */
    @NotNull(message = "userId不能为空")
    private String userId;

    /** legacy field: recent history sent from client */
    private List<ChatMessage> messages;

    /** optional client-reported IANA timezone id */
    private String timezone;

    /** optional client-reported UTC offset in minutes (JavaScript style) */
    private Integer utcOffsetMinutes;
}