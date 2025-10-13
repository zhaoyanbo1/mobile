package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.web.entity.UserMedal;
import com.kuafu.web.mapper.UserMedalMapper;
import com.kuafu.web.service.UserMedalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserMedalServiceImpl implements UserMedalService {

    private final UserMedalMapper userMedalMapper;

    public UserMedalServiceImpl(UserMedalMapper userMedalMapper){
        this.userMedalMapper = userMedalMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grant(Long userId, Long medalId) {
        // 方式一（高效）：依赖 UNIQUE 约束 + INSERT OR IGNORE（推荐）
        int rows = userMedalMapper.insertIgnore(userId, medalId);
        return rows == 1;

        // 方式二（传统）：先查再插（并发下可能重复，不如方式一可靠）
        /*
        long count = userMedalMapper.selectCount(
            new QueryWrapper<UserMedal>()
                .eq("user_id", userId)
                .eq("medal_id", medalId)
        );
        if (count > 0) return false;
        UserMedal um = new UserMedal();
        um.setUserId(userId); um.setMedalId(medalId);
        userMedalMapper.insert(um);
        return true;
        */
    }

    @Override
    public List<UserMedal> listByUser(Long userId) {
        return userMedalMapper.selectList(
                new QueryWrapper<UserMedal>()
                        .eq("user_id", userId)
                        .orderByDesc("created_at")
        );
    }
}
