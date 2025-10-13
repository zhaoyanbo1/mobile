package com.kuafu.web.service;

import com.kuafu.web.entity.UserMedal;

import java.util.List;

public interface UserMedalService {
    boolean grant(Long userId, Long medalId);
    /** 按用户查询已获得的奖章列表（按时间倒序） */
    List<UserMedal> listByUser(Long userId);
}
