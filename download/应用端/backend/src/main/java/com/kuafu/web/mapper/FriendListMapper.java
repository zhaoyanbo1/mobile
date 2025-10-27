package com.kuafu.web.mapper;

import com.kuafu.web.dto.FriendBrief;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendListMapper {

    /**
     * 获取指定用户的所有好友（根据 friendship 表）
     */
    List<FriendBrief> listMyFriends(@Param("userId") Long userId);
}
