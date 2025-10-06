package com.kuafu.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Message representation exposed to clients.
 */
@Data
@Builder
public class ConversationMessageDTO {
    private Long messageId;
    private String conversationId;
    private String role;
    private String name;
    private String content;
    private String status;
    private String finishReason;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private String errorMessage;
    private Date createTime;
    private Date updateTime;
}