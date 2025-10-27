package com.kuafu.web.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("friendship")
public class Friendship {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long friendUserId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
