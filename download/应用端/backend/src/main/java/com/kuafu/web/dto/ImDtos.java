package com.kuafu.web.dto;

import lombok.Data;
import java.util.List;

public class ImDtos {

    @Data
    public static class SendMsgReq {
        private Long conversationId; // 已有会话则填它
        private Long peerId;         // 没有会话时，填对端用户ID自动创建
        private String contentType;  // TEXT/IMAGE... 目前用 TEXT
        private String content;      // 文本内容 或 未来JSON
    }

    @Data
    public static class MessageResp {
        private Long messageId;
        private Long conversationId;
        private Long senderId;
        private String contentType;
        private String content;
        private Long createdAtEpochMs;
    }

    @Data
    public static class PageResult<T> {
        private List<T> list;
        private Long nextBeforeId; // 下一页游标（传给 beforeId），没有则为 null
    }
}
