package com.kuafu.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.web.dto.team.DecideTeamActivityRequest;
import com.kuafu.web.dto.team.TeamActivityOverviewResponse;
import com.kuafu.web.dto.team.TeamActivityReminderView;
import com.kuafu.web.dto.team.TeamActivityView;
import com.kuafu.web.dto.team.UserSummary;
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.entity.TeamActivity;
import com.kuafu.web.entity.TeamActivityApplication;
import com.kuafu.web.entity.TeamActivityParticipant;
import com.kuafu.web.entity.UserInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Domain coordinator encapsulating the business logic for team activities.
 */
@Service
@RequiredArgsConstructor
public class TeamActivityCoordinator {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final List<DateTimeFormatter> SUPPORTED_FORMATS = List.of(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_REJECTED = "REJECTED";

    private final ITeamActivityService teamActivityService;
    private final ITeamActivityParticipantService participantService;
    private final ITeamActivityApplicationService applicationService;
    private final IUserInfoService userInfoService;
    private final IReminderItemService reminderItemService;

    @Transactional
    public TeamActivityView create(Long userId, com.kuafu.web.dto.team.CreateTeamActivityRequest request) {
        validateCapacity(request.getMinParticipants(), request.getMaxParticipants());
        LocalDateTime activityTime = parseTime(request.getTime());
        LocalDateTime now = LocalDateTime.now();

        TeamActivity activity = TeamActivity.builder()
                .creatorUserId(userId)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .activityTime(activityTime)
                .location(request.getLocation().trim())
                .minParticipants(request.getMinParticipants())
                .maxParticipants(request.getMaxParticipants())
                .createdAt(now)
                .updatedAt(now)
                .build();
        teamActivityService.save(activity);

        TeamActivityParticipant host = TeamActivityParticipant.builder()
                .activityId(activity.getId())
                .userId(userId)
                .host(Boolean.TRUE)
                .joinedAt(now)
                .build();
        participantService.save(host);

        ensureReminderForUser(activity, userId);

        return loadSingleView(activity.getId(), userId);
    }

    @Transactional
    public TeamActivityView update(Long userId, Long activityId, com.kuafu.web.dto.team.UpdateTeamActivityRequest request) {
        TeamActivity activity = requireActivity(activityId);
        ensureHost(userId, activity);
        validateCapacity(request.getMinParticipants(), request.getMaxParticipants());
        LocalDateTime activityTime = parseTime(request.getTime());

        long participantCount = participantService.count(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activityId));
        if (participantCount > request.getMaxParticipants()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Max participants cannot be less than current participants");
        }

        activity.setTitle(request.getTitle().trim());
        activity.setDescription(request.getDescription().trim());
        activity.setActivityTime(activityTime);
        activity.setLocation(request.getLocation().trim());
        activity.setMinParticipants(request.getMinParticipants());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setUpdatedAt(LocalDateTime.now());
        teamActivityService.updateById(activity);

        syncReminders(activity);
        return loadSingleView(activityId, userId);
    }

    @Transactional
    public TeamActivityView apply(Long userId, Long activityId) {
        TeamActivity activity = requireActivity(activityId);
        if (Objects.equals(activity.getCreatorUserId(), userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "You are already hosting this activity");
        }
        boolean alreadyJoined = participantService.count(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activityId)
                .eq(TeamActivityParticipant::getUserId, userId)) > 0;
        if (alreadyJoined) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "You have already joined this activity");
        }

        long currentCount = participantService.count(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activityId));
        if (currentCount >= activity.getMaxParticipants()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "This activity is already full");
        }

        TeamActivityApplication application = applicationService.getOne(new LambdaQueryWrapper<TeamActivityApplication>()
                .eq(TeamActivityApplication::getActivityId, activityId)
                .eq(TeamActivityApplication::getApplicantUserId, userId)
                .last("LIMIT 1"));

        LocalDateTime now = LocalDateTime.now();
        if (application == null) {
            application = TeamActivityApplication.builder()
                    .activityId(activityId)
                    .applicantUserId(userId)
                    .status(STATUS_PENDING)
                    .createdAt(now)
                    .build();
            applicationService.save(application);
        } else if (STATUS_PENDING.equals(application.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "You already have a pending request");
        } else {
            application.setStatus(STATUS_PENDING);
            application.setCreatedAt(now);
            application.setDecisionAt(null);
            application.setDecisionBy(null);
            applicationService.updateById(application);
        }
        return loadSingleView(activityId, userId);
    }

    @Transactional
    public TeamActivityView decide(Long userId, Long activityId, DecideTeamActivityRequest request) {
        TeamActivity activity = requireActivity(activityId);
        ensureHost(userId, activity);

        TeamActivityApplication application = applicationService.getOne(new LambdaQueryWrapper<TeamActivityApplication>()
                .eq(TeamActivityApplication::getActivityId, activityId)
                .eq(TeamActivityApplication::getApplicantUserId, request.getApplicantId())
                .last("LIMIT 1"));
        if (application == null || !STATUS_PENDING.equals(application.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "No pending application found");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean approve = Boolean.TRUE.equals(request.getApprove());
        if (approve) {
            long currentCount = participantService.count(new LambdaQueryWrapper<TeamActivityParticipant>()
                    .eq(TeamActivityParticipant::getActivityId, activityId));
            if (currentCount >= activity.getMaxParticipants()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "This activity is already full");
            }

            application.setStatus(STATUS_APPROVED);
            application.setDecisionAt(now);
            application.setDecisionBy(userId);
            applicationService.updateById(application);

            participantService.save(TeamActivityParticipant.builder()
                    .activityId(activityId)
                    .userId(request.getApplicantId())
                    .host(Boolean.FALSE)
                    .joinedAt(now)
                    .build());

            ensureReminderForUser(activity, request.getApplicantId());
        } else {
            application.setStatus(STATUS_REJECTED);
            application.setDecisionAt(now);
            application.setDecisionBy(userId);
            applicationService.updateById(application);
        }

        activity.setUpdatedAt(now);
        teamActivityService.updateById(activity);
        return loadSingleView(activityId, userId);
    }

    public TeamActivityOverviewResponse overview(Long userId) {
        List<TeamActivityView> activities = buildViews(userId, null);
        List<TeamActivityReminderView> schedule = loadRemindersForUser(userId);
        UserInfo user = userInfoService.getById(userId);
        UserSummary current = user == null ? new UserSummary(userId, "")
                : new UserSummary(user.getUserInfoId().longValue(), Optional.ofNullable(user.getUsername()).orElse(""));
        return TeamActivityOverviewResponse.builder()
                .currentUser(current)
                .activities(activities)
                .schedule(schedule)
                .build();
    }

    public List<TeamActivityView> managedActivities(Long userId) {
        return buildViews(userId, Boolean.TRUE);
    }

    private List<TeamActivityView> buildViews(Long userId, Boolean onlyHost) {
        List<TeamActivity> activities = teamActivityService.list(new LambdaQueryWrapper<TeamActivity>()
                .orderByAsc(TeamActivity::getActivityTime));
        if (CollectionUtils.isEmpty(activities)) {
            return List.of();
        }
        List<Long> activityIds = activities.stream().map(TeamActivity::getId).collect(Collectors.toList());

        List<TeamActivityParticipant> participants = participantService.list(new LambdaQueryWrapper<TeamActivityParticipant>()
                .in(TeamActivityParticipant::getActivityId, activityIds));
        Map<Long, List<TeamActivityParticipant>> participantsByActivity = participants.stream()
                .collect(Collectors.groupingBy(TeamActivityParticipant::getActivityId));

        List<TeamActivityApplication> applications = applicationService.list(new LambdaQueryWrapper<TeamActivityApplication>()
                .in(TeamActivityApplication::getActivityId, activityIds));
        Map<Long, List<TeamActivityApplication>> applicationsByActivity = applications.stream()
                .collect(Collectors.groupingBy(TeamActivityApplication::getActivityId));

        Set<Long> userIds = new HashSet<>();
        userIds.addAll(activities.stream().map(TeamActivity::getCreatorUserId).collect(Collectors.toSet()));
        participants.forEach(p -> userIds.add(p.getUserId()));
        applications.forEach(a -> userIds.add(a.getApplicantUserId()));

        Map<Long, UserSummary> users = loadUserSummaries(userIds);

        return activities.stream()
                .map(activity -> toView(activity, participantsByActivity.get(activity.getId()),
                        applicationsByActivity.get(activity.getId()), users, userId))
                .filter(view -> onlyHost == null || view.isHost() == onlyHost)
                .sorted(Comparator.comparing(TeamActivityView::getActivityTimeIso, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }

    private TeamActivityView loadSingleView(Long activityId, Long userId) {
        TeamActivity activity = requireActivity(activityId);
        List<TeamActivityParticipant> participants = participantService.list(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activityId));
        List<TeamActivityApplication> applications = applicationService.list(new LambdaQueryWrapper<TeamActivityApplication>()
                .eq(TeamActivityApplication::getActivityId, activityId));

        Set<Long> userIds = new HashSet<>();
        userIds.add(activity.getCreatorUserId());
        participants.forEach(p -> userIds.add(p.getUserId()));
        applications.forEach(a -> userIds.add(a.getApplicantUserId()));
        Map<Long, UserSummary> users = loadUserSummaries(userIds);
        return toView(activity, participants, applications, users, userId);
    }

    private TeamActivityView toView(TeamActivity activity,
                                    Collection<TeamActivityParticipant> participantCollection,
                                    Collection<TeamActivityApplication> applicationCollection,
                                    Map<Long, UserSummary> users,
                                    Long currentUserId) {
        List<TeamActivityParticipant> participants = participantCollection == null
                ? List.of()
                : new ArrayList<>(participantCollection);
        List<TeamActivityApplication> applications = applicationCollection == null
                ? List.of()
                : new ArrayList<>(applicationCollection);

        boolean host = participants.stream()
                .anyMatch(p -> Boolean.TRUE.equals(p.getHost()) && Objects.equals(p.getUserId(), currentUserId));
        boolean joined = participants.stream()
                .anyMatch(p -> Objects.equals(p.getUserId(), currentUserId));
        boolean pending = applications.stream()
                .anyMatch(a -> Objects.equals(a.getApplicantUserId(), currentUserId) && STATUS_PENDING.equals(a.getStatus()));

        List<UserSummary> participantViews = participants.stream()
                .map(p -> users.getOrDefault(p.getUserId(), new UserSummary(p.getUserId(), "")))
                .collect(Collectors.toList());
        List<UserSummary> pendingViews = applications.stream()
                .filter(a -> STATUS_PENDING.equals(a.getStatus()))
                .map(a -> users.getOrDefault(a.getApplicantUserId(), new UserSummary(a.getApplicantUserId(), "")))
                .collect(Collectors.toList());

        UserSummary creator = users.getOrDefault(activity.getCreatorUserId(),
                new UserSummary(activity.getCreatorUserId(), ""));

        String displayTime = activity.getActivityTime() == null ? null : DISPLAY_FORMAT.format(activity.getActivityTime());
        String isoTime = activity.getActivityTime() == null ? null : activity.getActivityTime().toString();

        return TeamActivityView.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .activityTime(displayTime)
                .activityTimeIso(isoTime)
                .location(activity.getLocation())
                .minParticipants(activity.getMinParticipants())
                .maxParticipants(activity.getMaxParticipants())
                .participantsCount(participants.size())
                .host(host)
                .joined(joined)
                .pending(pending)
                .full(participants.size() >= activity.getMaxParticipants())
                .creator(creator)
                .participants(participantViews)
                .pendingApplicants(pendingViews)
                .build();
    }

    private Map<Long, UserSummary> loadUserSummaries(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Map.of();
        }
        List<UserInfo> users = userInfoService.listByIds(userIds);
        Map<Long, UserSummary> map = new HashMap<>();
        for (UserInfo user : users) {
            Long id = user.getUserInfoId() == null ? null : user.getUserInfoId().longValue();
            if (id != null) {
                map.put(id, new UserSummary(id, Optional.ofNullable(user.getUsername()).orElse("")));
            }
        }
        return map;
    }

    private List<TeamActivityReminderView> loadRemindersForUser(Long userId) {
        List<ReminderItem> reminders = reminderItemService.list(new LambdaQueryWrapper<ReminderItem>()
                .eq(ReminderItem::getUserInfoUserInfoId1, safeInt(userId))
                .isNotNull(ReminderItem::getTeamActivityId)
                .orderByAsc(ReminderItem::getReminderTime));
        return reminders.stream()
                .map(item -> TeamActivityReminderView.builder()
                        .reminderId(item.getReminderItemId() == null ? null : item.getReminderItemId().longValue())
                        .activityId(item.getTeamActivityId() == null ? null : item.getTeamActivityId().longValue())
                        .userId(item.getUserInfoUserInfoId1() == null ? null : item.getUserInfoUserInfoId1().longValue())
                        .title(item.getTitle())
                        .time(item.getReminderTime() == null ? null : DISPLAY_FORMAT.format(toLocalDateTime(item.getReminderTime())))
                        .timeIso(item.getReminderTime() == null ? null : toLocalDateTime(item.getReminderTime()).toString())
                        .location(item.getLocationAddress())
                        .build())
                .collect(Collectors.toList());
    }

    private void ensureReminderForUser(TeamActivity activity, Long userId) {
        Integer userIdInt = safeInt(userId);
        ReminderItem existing = reminderItemService.getOne(new LambdaQueryWrapper<ReminderItem>()
                .eq(ReminderItem::getUserInfoUserInfoId1, userIdInt)
                .eq(ReminderItem::getTeamActivityId, safeInt(activity.getId()))
                .last("LIMIT 1"));
        Date reminderTime = activity.getActivityTime() == null ? null : Date.from(activity.getActivityTime()
                .atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        if (existing == null) {
            ReminderItem item = ReminderItem.builder()
                    .userInfoUserInfoId1(userIdInt)
                    .reminderTypeEnumId(2)
                    .title(activity.getTitle())
                    .description(activity.getDescription())
                    .reminderTime(reminderTime)
                    .isCompleted(Boolean.FALSE)
                    .locationAddress(activity.getLocation())
                    .creationTime(now)
                    .updateTime(now)
                    .teamActivityId(safeInt(activity.getId()))
                    .build();
            reminderItemService.save(item);
        } else {
            existing.setTitle(activity.getTitle());
            existing.setDescription(activity.getDescription());
            existing.setReminderTime(reminderTime);
            existing.setLocationAddress(activity.getLocation());
            existing.setUpdateTime(now);
            reminderItemService.updateById(existing);
        }
    }

    private void syncReminders(TeamActivity activity) {
        List<TeamActivityParticipant> participants = participantService.list(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activity.getId()));
        for (TeamActivityParticipant participant : participants) {
            ensureReminderForUser(activity, participant.getUserId());
        }
    }

    private TeamActivity requireActivity(Long activityId) {
        TeamActivity activity = teamActivityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Activity not found");
        }
        return activity;
    }

    private void ensureHost(Long userId, TeamActivity activity) {
        boolean isHost = participantService.count(new LambdaQueryWrapper<TeamActivityParticipant>()
                .eq(TeamActivityParticipant::getActivityId, activity.getId())
                .eq(TeamActivityParticipant::getUserId, userId)
                .eq(TeamActivityParticipant::getHost, Boolean.TRUE)) > 0;
        if (!isHost) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Only the host can manage this activity");
        }
    }

    private void validateCapacity(Integer min, Integer max) {
        if (min == null || max == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Capacity is required");
        }
        if (min < 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "At least two people are required");
        }
        if (max > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Maximum group size is ten");
        }
        if (min > max) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Min participants cannot exceed max");
        }
    }

    private LocalDateTime parseTime(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Activity time is required");
        }
        String value = input.trim();
        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // try next format
            }
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "Unsupported time format. Use yyyy-MM-dd HH:mm");
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private Integer safeInt(Long value) {
        return value == null ? null : Math.toIntExact(value);
    }
}