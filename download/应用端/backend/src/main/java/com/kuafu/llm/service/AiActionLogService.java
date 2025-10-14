package com.kuafu.llm.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.llm.entity.AiActionLog;
import com.kuafu.llm.mapper.AiActionLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class AiActionLogService {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_DECLINED = "DECLINED";
    public static final String STATUS_FAILED = "FAILED";

    private final AiActionLogMapper actionLogMapper;

    public AiActionLogService(AiActionLogMapper actionLogMapper) {
        this.actionLogMapper = actionLogMapper;
    }

    public AiActionLog createPendingLog(String conversationId, String userId, String toolName, String requestPayload) {
        Date now = new Date();
        AiActionLog log = AiActionLog.builder()
                .conversationId(conversationId)
                .userId(userId)
                .toolName(toolName)
                .status(STATUS_PENDING)
                .requestPayload(requestPayload)
                .createdAt(now)
                .updatedAt(now)
                .build();
        actionLogMapper.insert(log);
        return log;
    }

    public AiActionLog requireLog(Long id) {
        AiActionLog log = actionLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Action log not found");
        }
        return log;
    }

    public AiActionLog requirePendingLog(Long id, String conversationId, String userId, String toolName) {
        AiActionLog log = requireLog(id);
        if (!STATUS_PENDING.equals(log.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Action already processed");
        }
        if (StringUtils.hasText(conversationId) && !conversationId.equals(log.getConversationId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Conversation mismatch");
        }
        if (StringUtils.hasText(userId) && !userId.equals(log.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "User mismatch");
        }
        if (StringUtils.hasText(toolName) && !toolName.equals(log.getToolName())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Tool mismatch");
        }
        return log;
    }

    public void markApproved(Long id, String responsePayload) {
        updateStatus(id, STATUS_APPROVED, responsePayload, null);
    }

    public void markDeclined(Long id, String responsePayload) {
        updateStatus(id, STATUS_DECLINED, responsePayload, null);
    }

    public void markFailed(Long id, String errorMessage) {
        updateStatus(id, STATUS_FAILED, null, errorMessage);
    }

    private void updateStatus(Long id, String status, String responsePayload, String errorMessage) {
        Date now = new Date();
        LambdaUpdateWrapper<AiActionLog> updateWrapper = Wrappers.lambdaUpdate(AiActionLog.class)
                .eq(AiActionLog::getId, id)
                .set(AiActionLog::getStatus, status)
                .set(AiActionLog::getResponsePayload, responsePayload)
                .set(AiActionLog::getErrorMessage, errorMessage)
                .set(AiActionLog::getExecutedAt, now)
                .set(AiActionLog::getUpdatedAt, now);
        actionLogMapper.update(null, updateWrapper);
    }

    public void appendError(Long id, String errorMessage) {
        LambdaUpdateWrapper<AiActionLog> updateWrapper = Wrappers.lambdaUpdate(AiActionLog.class)
                .eq(AiActionLog::getId, id)
                .set(AiActionLog::getErrorMessage, errorMessage)
                .set(AiActionLog::getUpdatedAt, new Date());
        actionLogMapper.update(null, updateWrapper);
    }
}