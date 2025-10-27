package com.kuafu.web.mapper;

import com.kuafu.web.model.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface FriendRequestMapper {
    int insert(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId);
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    FriendRequest selectById(@Param("id") Long id);
    FriendRequest selectByPair(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId);

    List<FriendRequest> findPendingBetween(@Param("me") Long me, @Param("targets") Set<Long> targets);
}

