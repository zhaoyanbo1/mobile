package com.kuafu.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 从 Spring Security 中解析当前登录用户ID。
 * 请根据你项目的 Principal 类型调整获取逻辑。
 */
public class SecurityUtils {
    public static Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new RuntimeException("Unauthenticated");
        Object p = auth.getPrincipal();
        if (p instanceof Long) return (Long) p;
        if (p instanceof Integer) return ((Integer) p).longValue();
        if (p instanceof String) {
            try { return Long.valueOf((String) p); } catch (Exception ignore) {}
        }
        // 如果你有自定义 UserPrincipal，可在此解析
        throw new RuntimeException("Cannot resolve current user id from principal: " + p);
    }
}
