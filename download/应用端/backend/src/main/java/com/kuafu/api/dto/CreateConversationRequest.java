package com.kuafu.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request body for creating a new conversation.
 */
@Data
public class CreateConversationRequest {

    @NotBlank(message = "userId不能为空")
    private String userId;

    private String title;
}