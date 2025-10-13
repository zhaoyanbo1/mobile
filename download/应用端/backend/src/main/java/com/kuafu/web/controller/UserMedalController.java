package com.kuafu.web.controller;

import com.kuafu.web.entity.UserMedal;
import com.kuafu.web.service.UserMedalService;
import com.kuafu.web.vo.MedalCollectReqVO;
import com.kuafu.web.vo.MedalCollectRespVO;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-medals")
public class UserMedalController {

    private final UserMedalService userMedalService;
    public UserMedalController(UserMedalService userMedalService){
        this.userMedalService = userMedalService;
    }

    /**
     * 前端上传 medalId（以及可选 userId）
     * 如果你有 JWT，这里可从 token 里取 uid 覆盖 req.userId
     */
    @PostMapping
    public ResponseEntity<MedalCollectRespVO> collect(@Valid @RequestBody MedalCollectReqVO req,
                                                    @RequestAttribute(name="uid", required=false) Long uidFromToken) {

        Long userId = (uidFromToken != null) ? uidFromToken : req.getUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().build(); // 没有用户信息
        }

        boolean created = userMedalService.grant(userId, req.getMedalId());
        return ResponseEntity.ok(new MedalCollectRespVO(created));
    }

    /**
     * （可选）查询用户已获得的奖章列表
     */
     @GetMapping
     public ResponseEntity<List<UserMedal>> list(@RequestParam Long userId) {
         List<UserMedal> list = userMedalService.listByUser(userId);
         return ResponseEntity.ok(list);
     }
}
