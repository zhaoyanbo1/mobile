package com.kuafu.llm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuafu.api.dto.TodoToolCallPayload;
import com.kuafu.api.dto.TodoToolExecutionResult;
import com.kuafu.api.dto.TodoToolPayloadView;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.llm.entity.AiActionLog;
import com.kuafu.llm.service.AiActionLogService;
import com.kuafu.llm.service.ChatService;
import com.kuafu.llm.service.ConversationService;
import com.kuafu.llm.util.TokenUtils;
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IReminderItemService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TodoToolService {

    private static final DateTimeFormatter[] DATE_PATTERNS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    };

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IReminderItemService reminderItemService;
    private final ConversationService conversationService;
    private final ChatService chatService;
    private final AiActionLogService actionLogService;

    public TodoToolService(IReminderItemService reminderItemService,
                           ConversationService conversationService,
                           ChatService chatService,
                           AiActionLogService actionLogService) {
        this.reminderItemService = reminderItemService;
        this.conversationService = conversationService;
        this.chatService = chatService;
        this.actionLogService = actionLogService;
    }

    public TodoToolExecutionResult executeTodoAction(AiActionLog log, TodoToolCallPayload payload) {
        ReminderItem item = buildReminderItem(log.getUserId(), payload);
        reminderItemService.save(item);

        TodoToolPayloadView view = TodoToolPayloadView.builder()
                .reminderItemId(item.getReminderItemId())
                .type(payload.getType())
                .title(item.getTitle())
                .dueAt(payload.getDueAt())
                .priority(item.getPriority())
                .notes(payload.getNotes())
                .dosage(item.getMedicineDosage())
                .build();

        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("status", "SUCCESS");
        responsePayload.put("todo", view);
        persistToolResultMessage(log.getConversationId(), log.getUserId(), "todo.create", responsePayload);

        actionLogService.markApproved(log.getId(), toJson(responsePayload));
        String assistantMessage = generateFollowUpMessage(log.getConversationId(), log.getUserId());
        return TodoToolExecutionResult.builder()
                .todo(view)
                .assistantMessage(assistantMessage)
                .build();
    }

    public TodoToolExecutionResult declineTodoAction(AiActionLog log, String reason) {
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("status", "DECLINED");
        if (StringUtils.hasText(reason)) {
            responsePayload.put("reason", reason);
        }
        actionLogService.markDeclined(log.getId(), toJson(responsePayload));
        persistToolResultMessage(log.getConversationId(), log.getUserId(), "todo.create", responsePayload);
        String assistantMessage = generateFollowUpMessage(log.getConversationId(), log.getUserId());
        return TodoToolExecutionResult.builder()
                .assistantMessage(assistantMessage)
                .build();
    }

    public String failAction(AiActionLog log, String errorMessage) {
        actionLogService.markFailed(log.getId(), errorMessage);
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("status", "FAILED");
        responsePayload.put("error", errorMessage);
        persistToolResultMessage(log.getConversationId(), log.getUserId(), "todo.create", responsePayload);
        return generateFollowUpMessage(log.getConversationId(), log.getUserId());
    }

    private ReminderItem buildReminderItem(String userId, TodoToolCallPayload payload) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Missing userId");
        }
        if (payload == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid todo request");
        }
        String type = payload.getType();
        if (!StringUtils.hasText(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Todo type is required");
        }
        Integer typeId = resolveTypeId(type.trim().toLowerCase());
        String title = payload.getTitle();
        if (!StringUtils.hasText(title)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Todo title is required");
        }
        Date dueTime = parseDueAt(payload.getDueAt());
        if (dueTime == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid due_at format");
        }
        ReminderItem item = ReminderItem.builder()
                .userInfoUserInfoId1(parseUserId(userId))
                .reminderTypeEnumId(typeId)
                .title(title.trim())
                .description(StringUtils.hasText(payload.getNotes()) ? payload.getNotes().trim() : null)
                .reminderTime(dueTime)
                .isCompleted(Boolean.FALSE)
                .medicineDosage(StringUtils.hasText(payload.getDosage()) ? payload.getDosage().trim() : null)
                .priority(normalizePriority(payload.getPriority()))
                .creationTime(new Date())
                .updateTime(new Date())
                .build();
        return item;
    }

    private Integer parseUserId(String userId) {
        try {
            return Integer.parseInt(userId);
        } catch (NumberFormatException ex) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid user id");
        }
    }

    private Integer resolveTypeId(String type) {
        if ("medication".equals(type)) {
            return 1;
        }
        if ("activity".equals(type)) {
            return 2;
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "Unsupported todo type: " + type);
    }

    private Date parseDueAt(String dueAt) {
        if (!StringUtils.hasText(dueAt)) {
            return null;
        }
        String trimmed = dueAt.trim();
        for (DateTimeFormatter formatter : DATE_PATTERNS) {
            try {
                if (formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME) {
                    OffsetDateTime odt = OffsetDateTime.parse(trimmed, formatter);
                    return Date.from(odt.toInstant());
                }
                LocalDateTime ldt = LocalDateTime.parse(trimmed, formatter);
                return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private String normalizePriority(String priority) {
        if (!StringUtils.hasText(priority)) {
            return "medium";
        }
        String p = priority.trim().toLowerCase();
        switch (p) {
            case "low":
            case "medium":
            case "high":
                return p;
            default:
                return "medium";
        }
    }

    private void persistToolResultMessage(String conversationId, String userId, String toolName, Map<String, Object> payload) {
        String content = toJson(payload);
        conversationService.appendToolMessage(conversationId, userId, toolName, content);
    }

    private String generateFollowUpMessage(String conversationId, String userId) {
        Long assistantId = conversationService.beginAssistantMessage(conversationId, userId);
        String prompt = conversationService.buildPrompt(conversationId);
        int promptTokens = TokenUtils.estimateTokens(prompt);
        StringBuilder builder = new StringBuilder();
        try (var stream = chatService.streamChat(prompt)) {
            stream.stream().forEach(event -> event.outputTextDelta().ifPresent(delta -> builder.append(delta.delta())));
        } catch (Exception ex) {
            conversationService.markAssistantMessageError(assistantId, builder.toString(), ex.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to generate follow-up message");
        }
        String text = sanitizeToolArtifacts(builder.toString());
        conversationService.updateAssistantMessageContent(assistantId, text);
        int completionTokens = TokenUtils.estimateTokens(text);
        conversationService.finalizeAssistantMessage(assistantId, text, promptTokens, completionTokens, "stop");
        return text;
    }

    private String sanitizeToolArtifacts(String text) {
        if (!StringUtils.hasText(text)) {
            return text;
        }
        return text.replaceAll("<<CALL_TODO\\s*\\{.*?}>>", "").trim();
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Serialization error");
        }
    }
}