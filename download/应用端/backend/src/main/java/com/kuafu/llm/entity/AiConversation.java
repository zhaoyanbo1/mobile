package com.kuafu.llm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistent conversation metadata for AI chats.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ai_conversation")
public class AiConversation implements Serializable {

    @TableId(value = "conversation_id", type = IdType.INPUT)
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

    private static final long serialVersionUID = 1L;
}