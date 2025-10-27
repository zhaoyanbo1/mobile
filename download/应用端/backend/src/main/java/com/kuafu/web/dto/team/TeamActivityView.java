package com.kuafu.web.dto.team;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Aggregated view returned for each team activity.
 */
@Value
@Builder
public class TeamActivityView {
    Long id;
    String title;
    String description;
    String activityTime;
    String activityTimeIso;
    String location;
    Integer minParticipants;
    Integer maxParticipants;
    Integer participantsCount;
    boolean host;
    boolean joined;
    boolean pending;
    boolean full;
    UserSummary creator;
    List<UserSummary> participants;
    List<UserSummary> pendingApplicants;
}