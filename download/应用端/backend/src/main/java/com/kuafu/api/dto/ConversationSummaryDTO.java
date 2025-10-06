package com.kuafu.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Summary info used to list conversations.
 */
@Data
@Builder
public class ConversationSummaryDTO {
    private String conversationId;
    private String userId;
    private String title;
    private String status;
    private Integer messageCount;
    private Date lastMessageTime;
    private Date archivedAt;
    private Date deletedAt;
    private Date createTime;
    private Date updateTime;
}