package com.kuafu.web.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@TableName("im_dm_pair")
public class ImDmPair {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userA; // 始终满足 userA < userB
    private Long userB;

    private Long conversationId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
