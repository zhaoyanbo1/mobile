package com.kuafu.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class FriendBrief {
    private Long userId;
    private String nickname;   // 没有就用 username
    private String avatarUrl;  // 没有就前端给默认头像
}
