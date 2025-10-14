package com.kuafu.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuafu.api.dto.TodoToolCallPayload;
import com.kuafu.api.dto.TodoToolDeclineRequest;
import com.kuafu.api.dto.TodoToolExecutionRequest;
import com.kuafu.api.dto.TodoToolExecutionResult;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.login.LoginUser;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.llm.entity.AiActionLog;
import com.kuafu.llm.service.AiActionLogService;
import com.kuafu.llm.service.TodoToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/chat/tools/todos")
public class ChatToolController {

    private static final Logger log = LoggerFactory.getLogger(ChatToolController.class);
    private final AiActionLogService actionLogService;
    private final TodoToolService todoToolService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatToolController(AiActionLogService actionLogService, TodoToolService todoToolService) {
        this.actionLogService = actionLogService;
        this.todoToolService = todoToolService;
    }

    @PostMapping("/execute")
    public BaseResponse<TodoToolExecutionResult> execute(@RequestBody TodoToolExecutionRequest request) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String currentUserId = resolveUserId(loginUser);
        AiActionLog logRecord = actionLogService.requirePendingLog(request.getActionLogId(), request.getConversationId(), currentUserId, "todo.create");
        TodoToolCallPayload payload = parsePayload(logRecord.getRequestPayload());
        try {
            TodoToolExecutionResult result = todoToolService.executeTodoAction(logRecord, payload);
            return ResultUtils.success(result);
        } catch (BusinessException ex) {
            log.warn("Todo execution failed", ex);
            String message = todoToolService.failAction(logRecord, ex.getMessage());
            TodoToolExecutionResult fallback = TodoToolExecutionResult.builder()
                    .assistantMessage(message)
                    .build();
            return new BaseResponse<>(ex.getCode(), fallback, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error executing todo tool", ex);
            String message = todoToolService.failAction(logRecord, "System error");
            TodoToolExecutionResult fallback = TodoToolExecutionResult.builder()
                    .assistantMessage(message)
                    .build();
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), fallback, "System error");
        }
    }

    @PostMapping("/decline")
    public BaseResponse<TodoToolExecutionResult> decline(@RequestBody TodoToolDeclineRequest request) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String currentUserId = resolveUserId(loginUser);
        AiActionLog logRecord = actionLogService.requirePendingLog(request.getActionLogId(), request.getConversationId(), currentUserId, "todo.create");
        parsePayload(logRecord.getRequestPayload());
        TodoToolExecutionResult result = todoToolService.declineTodoAction(logRecord, request.getReason());
        return ResultUtils.success(result);
    }

    private String resolveUserId(LoginUser loginUser) {
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "User not logged in");
        }
        if (Objects.nonNull(loginUser.getRelevanceId())) {
            return loginUser.getRelevanceId();
        }
        return String.valueOf(loginUser.getUserId());
    }

    private TodoToolCallPayload parsePayload(String json) {
        try {
            return objectMapper.readValue(json, TodoToolCallPayload.class);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid tool payload");
        }
    }
}