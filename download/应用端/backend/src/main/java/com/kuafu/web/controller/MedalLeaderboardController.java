package com.kuafu.web.controller;

import com.kuafu.login.annotation.IgnoreAuth;
import com.kuafu.web.service.MedalLeaderboardService;
import com.kuafu.web.vo.MedalLeaderboardVO;
import com.kuafu.web.vo.MyMedalStatVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@RestController
//@RequestMapping("/api/medals")
//public class MedalLeaderboardController {
//
//    private final MedalLeaderboardService service;
//
//    public MedalLeaderboardController(MedalLeaderboardService service) {
//        this.service = service;
//    }
//    @IgnoreAuth
//    @GetMapping("/leaderboard")
//    public Object leaderboard(@RequestParam(defaultValue = "1") int page,
//                              @RequestParam(defaultValue = "20") int size,
//                              @RequestParam(value = "userId", required = false) Long userId,
//                              Authentication auth) {
////        Long userId = extractUserId(auth); // TODO: 用你项目的方式获取
////        if (userId == null) {
////            userId = extractUserId(auth);
////        }
////
////        List<MedalLeaderboardVO> rows = service.page(page, size);
////        long total = service.totalUsersOnBoard();
////        MyMedalStatVO me = (userId != null) ? service.myStat(userId) : null;
////
////        Map<String, Object> data = new HashMap<>();
////        data.put("page", page);
////        data.put("size", size);
////        data.put("total", total);
////        data.put("list", rows);
////        data.put("me", me);
////        return data; // 有统一返回体的话：Result.ok(data)
//        if (userId == null) userId = extractUserId(auth); // 带 token 时也能取
//        var rows = service.page(page, size);
//        var total = service.totalUsersOnBoard();
//        var me = (userId != null) ? service.myStat(userId) : null;
//        Map<String,Object> data = new HashMap<>();
//        data.put("page", page); data.put("size", size);
//        data.put("total", total); data.put("list", rows); data.put("me", me);
//        return data;
//    }
//
//    private Long extractUserId(Authentication auth) {
//        if (auth == null) return null;
//        // 示例：return ((LoginUser) auth.getPrincipal()).getUserId();
//        return null;
//    }
//}
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leaderboard")
public class MedalLeaderboardController {

    private final MedalLeaderboardService leaderboardService;

    @GetMapping("/page")
    public LeaderboardPageResp page(
            @RequestParam(required = false) Long userId,   // 前端传当前用户id
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        Long me = userId;
        List<MedalLeaderboardVO> list = leaderboardService.pageWithFriendStatus(me, pageNo, pageSize);
        MyMedalStatVO my = leaderboardService.myMedalStat(me);

        LeaderboardPageResp resp = new LeaderboardPageResp();
        resp.setList(list);
        resp.setTotal(leaderboardService.countLeaderboardUsers());
        resp.setMy(my);
        return resp;
    }

    @Data
    public static class LeaderboardPageResp {
        private List<MedalLeaderboardVO> list;
        private long total;
        private MyMedalStatVO my;
    }
}
