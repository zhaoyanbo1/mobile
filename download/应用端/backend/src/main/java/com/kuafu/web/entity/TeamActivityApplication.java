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
 * Join application submitted by a user for a team activity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("team_activity_application")
public class TeamActivityApplication {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("activity_id")
    private Long activityId;

    @TableField("applicant_user_id")
    private Long applicantUserId;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("decision_at")
    private LocalDateTime decisionAt;

    @TableField("decision_by")
    private Long decisionBy;
}