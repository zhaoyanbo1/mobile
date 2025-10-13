package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

@TableName("user_medal")
public class UserMedal {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("medal_id")
    private Long medalId;

    @TableField("created_at")
    private Date createdAt;

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getUserId(){ return userId; }
    public void setUserId(Long userId){ this.userId = userId; }
    public Long getMedalId(){ return medalId; }
    public void setMedalId(Long medalId){ this.medalId = medalId; }
    public Date getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Date createdAt){ this.createdAt = createdAt; }
}
