package com.kuafu.web.controller;

import com.kuafu.web.dto.FriendDtos.*;
import com.kuafu.web.model.FriendRequest;
import com.kuafu.web.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    // 模拟用户登录（正式项目请改为从 JWT 解析）
    private Long currentUserId(@RequestHeader(value = "X-User-Id", required = false) Long mockId) {
        if (mockId == null) throw new RuntimeException("请在请求头里传 X-User-Id 或接入 JWT");
        return mockId;
    }

    @PostMapping("/requests")
    public FriendRequestResp createRequest(
            @RequestHeader(value = "X-User-Id", required = false) Long mockId,
            @RequestBody CreateRequestReq req) {
        Long me = currentUserId(mockId);
        FriendRequest fr = friendService.createRequest(me, req.getReceiverId());
        FriendRequestResp resp = new FriendRequestResp();
        resp.setId(fr.getId());
        resp.setRequesterId(fr.getRequesterId());
        resp.setReceiverId(fr.getReceiverId());
        resp.setStatus(fr.getStatus());
        return resp;
    }

    @PostMapping("/requests/{id}/accept")
    public void accept(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                       @PathVariable Long id) {
        friendService.accept(currentUserId(mockId), id);
    }

    @PostMapping("/requests/{id}/decline")
    public void decline(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                        @PathVariable Long id) {
        friendService.decline(currentUserId(mockId), id);
    }

    @PostMapping("/requests/{id}/cancel")
    public void cancel(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                       @PathVariable Long id) {
        friendService.cancel(currentUserId(mockId), id);
    }

    /** 批量获取好友状态（排行榜用） */
    @PostMapping("/statuses")
    public Map<String, Object> batchStatuses(
            @RequestHeader(value = "X-User-Id", required = false) Long mockId,
            @RequestBody StatusBatchReq req) {

        Long me = currentUserId(mockId);

        // ✅ 关键修复：List → Set
        Set<Long> userIdSet = new HashSet<>(req.getUserIds());

        return Map.of(
                "statuses", friendService.batchStatuses(me, userIdSet),
                "requestIds", friendService.batchPendingRequestIds(me, userIdSet)
        );
    }
}
