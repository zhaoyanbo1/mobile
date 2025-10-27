package com.kuafu.web.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_message")
public class ImMessage {
    @TableId(type = IdType.AUTO)
    private Long messageId;

    private Long conversationId;
    private Long senderId;

    private String contentType; // TEXT / IMAGE / FILE...
    private String content;

    private String status; // SENT/DELIVERED/READ 预留

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
