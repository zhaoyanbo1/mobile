package com.kuafu.llm.util;

import org.springframework.util.StringUtils;

/**
 * Utility helpers for lightweight token estimation.
 */
public final class TokenUtils {

    private TokenUtils() {
    }

    /**
     * Estimate token usage using a simple heuristic (4 characters per token) when
     * the upstream model does not return exact usage statistics.
     */
    public static int estimateTokens(String text) {
        if (!StringUtils.hasText(text)) {
            return 0;
        }
        int length = text.trim().length();
        int estimated = Math.max(1, length / 4);
        // guard against overflow and unreasonable spikes
        return Math.min(estimated, 1_000_000);
    }
}