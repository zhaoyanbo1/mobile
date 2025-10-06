package com.kuafu.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Cursor-based message page response.
 */
@Data
@Builder
public class MessagePageResponse {
    private List<ConversationMessageDTO> records;
    private Long nextCursor;
    private boolean hasMore;
}