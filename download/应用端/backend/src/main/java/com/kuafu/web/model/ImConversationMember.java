package com.kuafu.web.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@TableName("im_conversation_member")
public class ImConversationMember {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long conversationId;
    private Long userId;

    private Long lastReadMsgId; // 已读游标
    private Integer mute;       // 0/1 预留

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinedAt;
}
