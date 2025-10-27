package com.kuafu.web.dto.team;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Response payload containing activity feed and schedule entries.
 */
@Value
@Builder
public class TeamActivityOverviewResponse {
    UserSummary currentUser;
    List<TeamActivityView> activities;
    List<TeamActivityReminderView> schedule;
}