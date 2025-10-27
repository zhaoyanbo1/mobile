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
 * Participant entry for a team activity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("team_activity_participant")
public class TeamActivityParticipant {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("activity_id")
    private Long activityId;

    @TableField("user_id")
    private Long userId;

    @TableField("is_host")
    private Boolean host;

    @TableField("joined_at")
    private LocalDateTime joinedAt;
}