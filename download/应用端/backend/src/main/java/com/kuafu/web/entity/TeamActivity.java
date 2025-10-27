package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a team activity created by a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("team_activity")
public class TeamActivity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("creator_user_id")
    private Long creatorUserId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("activity_time")
    private LocalDateTime activityTime;

    @TableField("location")
    private String location;

    @TableField("min_participants")
    private Integer minParticipants;

    @TableField("max_participants")
    private Integer maxParticipants;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}