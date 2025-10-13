package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.UserMedal;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMedalMapper extends BaseMapper<UserMedal> {

    // SQLite: 如果已存在(user_id, medal_id)，这条插入会被忽略，返回 0 行受影响
    @Insert("INSERT OR IGNORE INTO user_medal (user_id, medal_id) VALUES (#{userId}, #{medalId})")
    int insertIgnore(@Param("userId") Long userId, @Param("medalId") Long medalId);
}
