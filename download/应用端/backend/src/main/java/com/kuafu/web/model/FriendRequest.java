package com.kuafu.web.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("friend_request")
public class FriendRequest {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long requesterId;
    private Long receiverId;

    // 用字符串存枚举名，兼容 SQLite
    private String status; // PENDING / ACCEPTED / DECLINED / CANCELED

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
