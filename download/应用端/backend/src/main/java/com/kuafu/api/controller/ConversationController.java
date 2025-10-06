package com.kuafu.api.controller;

import com.kuafu.api.dto.ConversationMessageDTO;
import com.kuafu.api.dto.ConversationSummaryDTO;
import com.kuafu.api.dto.CreateConversationRequest;
import com.kuafu.api.dto.MessagePageResponse;
import com.kuafu.api.dto.PageResult;
import com.kuafu.api.dto.RenameConversationRequest;
import com.kuafu.api.dto.UpdateConversationStatusRequest;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.llm.model.ConversationStatus;
import com.kuafu.llm.service.ConversationService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST endpoints for managing AI conversations.
 */
@RestController
@RequestMapping("/api/conversations")
@CrossOrigin
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public BaseResponse<ConversationSummaryDTO> create(@Valid @RequestBody CreateConversationRequest request) {
        String conversationId = conversationService.newConversation(request.getUserId(), request.getTitle());
        ConversationSummaryDTO summary = conversationService.getConversationSummary(conversationId, request.getUserId());
        return ResultUtils.success(summary);
    }

    @GetMapping
    public BaseResponse<PageResult<ConversationSummaryDTO>> page(
            @RequestParam("userId") String userId,
            @RequestParam(value = "page", defaultValue = "1") long current,
            @RequestParam(value = "size", defaultValue = "20") long size,
            @RequestParam(value = "status", required = false) String status
    ) {
        ConversationStatus conversationStatus = null;
        if (StringUtils.hasText(status)) {
            try {
                conversationStatus = ConversationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "status参数无效");
            }
        }
        PageResult<ConversationSummaryDTO> pageResult = conversationService.pageConversations(userId, current, size, conversationStatus);
        return ResultUtils.success(pageResult);
    }

    @PutMapping("/{conversationId}/title")
    public BaseResponse<Void> rename(
            @PathVariable String conversationId,
            @Valid @RequestBody RenameConversationRequest request
    ) {
        conversationService.renameConversation(conversationId, request.getUserId(), request.getTitle());
        return ResultUtils.success();
    }

    @PostMapping("/{conversationId}/archive")
    public BaseResponse<Void> archive(
            @PathVariable String conversationId,
            @Valid @RequestBody UpdateConversationStatusRequest request
    ) {
        conversationService.archiveConversation(conversationId, request.getUserId());
        return ResultUtils.success();
    }

    @PostMapping("/{conversationId}/restore")
    public BaseResponse<Void> restore(
            @PathVariable String conversationId,
            @Valid @RequestBody UpdateConversationStatusRequest request
    ) {
        conversationService.restoreConversation(conversationId, request.getUserId());
        return ResultUtils.success();
    }

    @DeleteMapping("/{conversationId}")
    public BaseResponse<Void> delete(
            @PathVariable String conversationId,
            @RequestParam("userId") String userId
    ) {
        conversationService.deleteConversation(conversationId, userId);
        return ResultUtils.success();
    }

    @GetMapping("/{conversationId}/messages")
    public BaseResponse<MessagePageResponse> messages(
            @PathVariable String conversationId,
            @RequestParam("userId") String userId,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        MessagePageResponse response = conversationService.pageMessages(conversationId, userId, cursor, limit);
        return ResultUtils.success(response);
    }

    @GetMapping("/{conversationId}/context")
    public BaseResponse<List<ConversationMessageDTO>> context(
            @PathVariable String conversationId,
            @RequestParam("userId") String userId
    ) {
        conversationService.requireConversation(conversationId, userId);
        List<ConversationMessageDTO> recent = conversationService.getRecentMessages(conversationId);
        return ResultUtils.success(recent);
    }
}