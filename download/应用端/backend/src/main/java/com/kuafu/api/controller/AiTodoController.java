package com.kuafu.api.controller;

import com.kuafu.api.dto.AiTodoSuggestionResult;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.login.LoginUser;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.llm.service.AiTodoSuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints that leverage the AI model to generate personalised todo tasks.
 */
@Slf4j
@RestController
@RequestMapping("/api/ai/todos")
@RequiredArgsConstructor
public class AiTodoController {

    private final AiTodoSuggestionService suggestionService;

    @PostMapping("/generate")
    public BaseResponse<AiTodoSuggestionResult> generate() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "User not logged in");
        }
        String userId = loginUser.getRelevanceId() != null
                ? String.valueOf(loginUser.getRelevanceId())
                : String.valueOf(loginUser.getUserId());
        AiTodoSuggestionResult result = suggestionService.generateSuggestions(userId);
        return ResultUtils.success(result);
    }

    @PostMapping("/generate-bonus")
    public BaseResponse<AiTodoSuggestionResult> generateBonus() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "User not logged in");
        }
        String userId = loginUser.getRelevanceId() != null
                ? String.valueOf(loginUser.getRelevanceId())
                : String.valueOf(loginUser.getUserId());
        AiTodoSuggestionResult result = suggestionService.generateBonusSuggestions(userId);
        return ResultUtils.success(result);
    }
}