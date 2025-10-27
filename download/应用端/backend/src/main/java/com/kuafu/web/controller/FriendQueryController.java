package com.kuafu.web.controller;

import com.kuafu.web.dto.FriendBrief;
import com.kuafu.web.mapper.FriendListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友列表查询接口
 * 前端调用 /api/friends/my 获取当前用户的好友列表
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendQueryController {

    private final FriendListMapper friendListMapper;

    private Long currentUserId(@RequestHeader(value = "X-User-Id", required = false) Long mockId) {
        if (mockId == null) throw new RuntimeException("请在请求头里传 X-User-Id 或接入 JWT");
        return mockId;
    }

    @GetMapping("/my")
    public List<FriendBrief> myFriends(
            @RequestHeader(value = "X-User-Id", required = false) Long mockId) {
        Long me = currentUserId(mockId);
        return friendListMapper.listMyFriends(me);
    }
}
