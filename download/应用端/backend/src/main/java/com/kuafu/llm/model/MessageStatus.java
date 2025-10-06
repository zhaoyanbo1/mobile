package com.kuafu.llm.model;

/**
 * Status marker for individual messages.
 */
public enum MessageStatus {
    STREAMING,
    FINAL,
    ERROR
}