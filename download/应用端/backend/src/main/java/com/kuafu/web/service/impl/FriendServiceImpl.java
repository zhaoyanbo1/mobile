package com.kuafu.web.service.impl;

import com.kuafu.web.mapper.FriendRequestMapper;
import com.kuafu.web.mapper.FriendshipMapper;
import com.kuafu.web.model.FriendRequest;
import com.kuafu.web.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRequestMapper friendRequestMapper;
    private final FriendshipMapper friendshipMapper;

    @Transactional
    @Override
    public FriendRequest createRequest(Long requesterId, Long receiverId) {
        if (Objects.equals(requesterId, receiverId)) {
            throw new IllegalArgumentException("不能加自己为好友");
        }
        friendRequestMapper.insert(requesterId, receiverId);
        return friendRequestMapper.selectByPair(requesterId, receiverId);
    }

    @Transactional
    @Override
    public void accept(Long me, Long requestId) {
        var req = friendRequestMapper.selectById(requestId);
        if (req == null || !"PENDING".equals(req.getStatus())) throw new IllegalStateException("请求不存在或已处理");
        if (!Objects.equals(req.getReceiverId(), me)) throw new SecurityException("无权操作");

        friendRequestMapper.updateStatus(requestId, "ACCEPTED");
        friendshipMapper.insertOne(req.getRequesterId(), req.getReceiverId());
        friendshipMapper.insertOne(req.getReceiverId(), req.getRequesterId());
    }

    @Transactional
    @Override
    public void decline(Long me, Long requestId) {
        var req = friendRequestMapper.selectById(requestId);
        if (req == null || !"PENDING".equals(req.getStatus())) throw new IllegalStateException("请求不存在或已处理");
        if (!Objects.equals(req.getReceiverId(), me)) throw new SecurityException("无权操作");
        friendRequestMapper.updateStatus(requestId, "DECLINED");
    }

    @Transactional
    @Override
    public void cancel(Long me, Long requestId) {
        var req = friendRequestMapper.selectById(requestId);
        if (req == null || !"PENDING".equals(req.getStatus())) throw new IllegalStateException("请求不存在或已处理");
        if (!Objects.equals(req.getRequesterId(), me)) throw new SecurityException("无权操作");
        friendRequestMapper.updateStatus(requestId, "CANCELED");
    }

    // ========= 排行榜需要：状态 + 申请ID =========

    @Override
    public Map<Long, String> batchStatuses(Long me, Set<Long> targets) {
        Map<Long, String> map = new HashMap<>();
        if (me == null || targets == null || targets.isEmpty()) return map;

        var friends = friendshipMapper.findFriendsBetween(me, targets);
        for (var f : friends) {
            map.put(f.getFriendUserId(), "FRIEND"); // ✅ 使用补齐后的 getter
        }

        var pendings = friendRequestMapper.findPendingBetween(me, targets);
        for (var r : pendings) {
            if (Objects.equals(r.getRequesterId(), me)) {
                map.put(r.getReceiverId(), "PENDING_OUT");
            } else if (Objects.equals(r.getReceiverId(), me)) {
                map.put(r.getRequesterId(), "PENDING_IN");
            }
        }
        return map;
    }

    @Override
    public Map<Long, Long> batchPendingRequestIds(Long me, Set<Long> targets) {
        Map<Long, Long> map = new HashMap<>();
        if (me == null || targets == null || targets.isEmpty()) return map;

        var pendings = friendRequestMapper.findPendingBetween(me, targets);
        for (var r : pendings) {
            if (Objects.equals(r.getRequesterId(), me)) {
                map.put(r.getReceiverId(), r.getId());
            } else if (Objects.equals(r.getReceiverId(), me)) {
                map.put(r.getRequesterId(), r.getId());
            }
        }
        return map;
    }
}
