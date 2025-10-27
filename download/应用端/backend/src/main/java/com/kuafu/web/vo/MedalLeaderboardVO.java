package com.kuafu.web.vo;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedalLeaderboardVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String username;
    private String avatarUrl;
    private Long medalCount;
    private Date lastMedalAt;

    private String friendStatus;   // NONE | PENDING_OUT | PENDING_IN | FRIEND
    private Long friendRequestId;  // PENDING 时的请求ID（用于同意/拒绝）
    private boolean me;            // 是否是本人（前端隐藏按钮）
}
