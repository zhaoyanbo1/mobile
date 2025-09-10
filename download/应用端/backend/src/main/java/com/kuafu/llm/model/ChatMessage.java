package com.kuafu.llm.model;

import lombok.Data;

/**
 * Simple chat message model holding role and content.
 */
@Data
public class ChatMessage {
    /** role of the message, e.g. user or assistant */
    private String role;

    /** actual message text */
    private String content;
}