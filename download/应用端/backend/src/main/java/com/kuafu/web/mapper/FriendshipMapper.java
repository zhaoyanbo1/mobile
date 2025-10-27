package com.kuafu.web.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface FriendshipMapper {

    int insertOne(@Param("userId") Long userId, @Param("friendUserId") Long friendUserId);

    boolean exists(@Param("userId") Long userId, @Param("friendUserId") Long friendUserId);

    List<FriendPair> findFriendsBetween(@Param("me") Long me,
                                        @Param("targets") Set<Long> targets);

    // ✅ 内部 DTO，用于 MyBatis 返回
    class FriendPair {
        private Long userId;
        private Long friendUserId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getFriendUserId() { return friendUserId; }
        public void setFriendUserId(Long friendUserId) { this.friendUserId = friendUserId; }
    }
}
