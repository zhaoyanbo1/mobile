package com.kuafu.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request body for updating a conversation status (archive/restore/delete).
 */
@Data
public class UpdateConversationStatusRequest {

    @NotBlank(message = "userId不能为空")
    private String userId;
}