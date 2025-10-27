package com.kuafu.web.dto.team;

import lombok.Builder;
import lombok.Value;

/**
 * View model for reminder entries linked to team activities.
 */
@Value
@Builder
public class TeamActivityReminderView {
    Long reminderId;
    Long activityId;
    Long userId;
    String title;
    String time;
    String timeIso;
    String location;
}