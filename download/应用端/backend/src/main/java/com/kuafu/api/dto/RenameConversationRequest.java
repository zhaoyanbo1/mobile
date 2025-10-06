package com.kuafu.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request for renaming a conversation.
 */
@Data
public class RenameConversationRequest {

    @NotBlank(message = "userId不能为空")
    private String userId;

    @NotBlank(message = "title不能为空")
    private String title;
}