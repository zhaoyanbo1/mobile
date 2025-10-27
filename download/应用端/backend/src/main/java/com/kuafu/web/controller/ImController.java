package com.kuafu.web.controller;

import com.kuafu.web.dto.ImDtos.MessageResp;
import com.kuafu.web.dto.ImDtos.PageResult;
import com.kuafu.web.dto.ImDtos.SendMsgReq;
import com.kuafu.web.service.ImService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/im")
public class ImController {

    private final ImService imService;

    private Long currentUserId(@RequestHeader(value = "X-User-Id", required = false) Long mockId) {
        if (mockId == null) throw new RuntimeException("请在请求头里传 X-User-Id 或接入 JWT");
        return mockId;
    }

    @PostMapping("/conversations/dm")
    public Long ensureDm(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                         @RequestParam Long peerId) {
        Long me = currentUserId(mockId);
        if (Objects.equals(me, peerId)) throw new IllegalArgumentException("不能与自己创建会话");
        return imService.ensureDmConversation(me, peerId);
    }

    @PostMapping("/messages")
    public MessageResp send(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                            @RequestBody SendMsgReq req) {
        Long me = currentUserId(mockId);
        return imService.send(me, req);
    }

    @GetMapping("/messages")
    public PageResult<MessageResp> history(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                                           @RequestParam Long conversationId,
                                           @RequestParam(required = false) Long beforeId,
                                           @RequestParam(defaultValue = "20") int limit) {
        Long me = currentUserId(mockId);
        return imService.history(me, conversationId, beforeId, limit);
    }

    @PostMapping("/read")
    public void markRead(@RequestHeader(value = "X-User-Id", required = false) Long mockId,
                         @RequestParam Long conversationId,
                         @RequestParam Long lastReadMsgId) {
        Long me = currentUserId(mockId);
        imService.markRead(me, conversationId, lastReadMsgId);
    }
}
