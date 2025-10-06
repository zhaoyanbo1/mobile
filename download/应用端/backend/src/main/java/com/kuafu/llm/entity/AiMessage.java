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
 * Persistent chat message belonging to an AI conversation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ai_message")
public class AiMessage implements Serializable {

    @TableId(value = "message_id", type = IdType.AUTO)
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

    private static final long serialVersionUID = 1L;
}