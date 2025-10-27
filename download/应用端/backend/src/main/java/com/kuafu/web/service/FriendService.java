package com.kuafu.web.service;

import com.kuafu.web.model.FriendRequest;

import java.util.Map;
import java.util.Set;

public interface FriendService {
    // 发起/处理
    FriendRequest createRequest(Long requesterId, Long receiverId);
    void accept(Long operatorUserId, Long requestId);
    void decline(Long operatorUserId, Long requestId);
    void cancel(Long operatorUserId, Long requestId);

    // 排行榜用：给一批 userIds 计算与“我”的关系，并给出待处理申请的 id
    Map<Long, String> batchStatuses(Long me, Set<Long> userIds);      // NONE / PENDING_OUT / PENDING_IN / FRIEND
    Map<Long, Long>   batchPendingRequestIds(Long me, Set<Long> userIds);
}
