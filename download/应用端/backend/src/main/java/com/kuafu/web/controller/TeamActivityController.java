package com.kuafu.web.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.web.dto.team.CreateTeamActivityRequest;
import com.kuafu.web.dto.team.DecideTeamActivityRequest;
import com.kuafu.web.dto.team.TeamActivityOverviewResponse;
import com.kuafu.web.dto.team.TeamActivityView;
import com.kuafu.web.dto.team.UpdateTeamActivityRequest;
import com.kuafu.web.service.TeamActivityCoordinator;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for collaborative team activities.
 */
@RestController
@RequestMapping("/api/team-activities")
@RequiredArgsConstructor
@Validated
public class TeamActivityController {

    private final TeamActivityCoordinator coordinator;

    @GetMapping
    public BaseResponse<TeamActivityOverviewResponse> overview(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @RequestParam(name = "userId", required = false) Long userIdParam) {
        Long userId = resolveUserId(uidFromToken, userIdParam);
        return ResultUtils.success(coordinator.overview(userId));
    }

    @GetMapping("/manage")
    public BaseResponse<List<TeamActivityView>> manage(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @RequestParam(name = "userId", required = false) Long userIdParam) {
        Long userId = resolveUserId(uidFromToken, userIdParam);
        return ResultUtils.success(coordinator.managedActivities(userId));
    }

    @PostMapping
    public BaseResponse<TeamActivityView> create(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @Valid @RequestBody CreateTeamActivityRequest request) {
        Long userId = resolveUserId(uidFromToken, request.getUserId());
        return ResultUtils.success(coordinator.create(userId, request));
    }

    @PutMapping("/{id}")
    public BaseResponse<TeamActivityView> update(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateTeamActivityRequest request) {
        Long userId = resolveUserId(uidFromToken, request.getUserId());
        return ResultUtils.success(coordinator.update(userId, id, request));
    }

    @PostMapping("/{id}/apply")
    public BaseResponse<TeamActivityView> apply(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @PathVariable("id") Long id,
            @RequestParam(name = "userId", required = false) Long userIdParam) {
        Long userId = resolveUserId(uidFromToken, userIdParam);
        return ResultUtils.success(coordinator.apply(userId, id));
    }

    @PostMapping("/{id}/decide")
    public BaseResponse<TeamActivityView> decide(
            @RequestAttribute(name = "uid", required = false) Long uidFromToken,
            @PathVariable("id") Long id,
            @Valid @RequestBody DecideTeamActivityRequest request) {
        Long userId = resolveUserId(uidFromToken, request.getUserId());
        return ResultUtils.success(coordinator.decide(userId, id, request));
    }

    private Long resolveUserId(Long uidFromToken, Long fallback) {
        if (uidFromToken != null) {
            return uidFromToken;
        }
        if (fallback != null) {
            return fallback;
        }
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "User is not authenticated");
    }
}