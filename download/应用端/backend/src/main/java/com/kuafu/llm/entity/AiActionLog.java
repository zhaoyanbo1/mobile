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
 * Audit log for AI initiated tool actions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ai_action_log")
public class AiActionLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String conversationId;

    private String userId;

    private String toolName;

    private String status;

    private String requestPayload;

    private String responsePayload;

    private String errorMessage;

    private Date createdAt;

    private Date updatedAt;

    private Date executedAt;

    private static final long serialVersionUID = 1L;
}