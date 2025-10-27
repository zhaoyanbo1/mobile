package com.kuafu.web.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

public class FriendDtos {
    @Data
    public static class CreateRequestReq { private Long receiverId; }

    @Data
    public static class FriendRequestResp {
        private Long id; private Long requesterId; private Long receiverId; private String status;
    }

    @Data
    public static class StatusBatchReq { private List<Long> userIds; }

    @Data
    public static class StatusBatchResp {
        // userId -> NONE | PENDING_OUT | PENDING_IN | FRIEND
        private Map<Long, String> statuses;
        // userId -> requestId
        private Map<Long, Long> requestIds;
    }
}