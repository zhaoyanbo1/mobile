package com.kuafu.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class FriendBrief {
    private Long userId;
    private String nickname;   // 没有就用 username
    private String avatarUrl;  // 没有就前端给默认头像

    /**
     * 最近一次与该好友的会话 ID（如果双方还未聊天则为空）。
     */
    private Long conversationId;

    /**
     * 当前用户尚未阅读的消息数量。
     */
    private Long unreadCount;
}
