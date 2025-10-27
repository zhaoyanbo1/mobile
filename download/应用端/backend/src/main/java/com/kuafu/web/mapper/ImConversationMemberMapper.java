package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.model.ImConversationMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImConversationMemberMapper extends BaseMapper<ImConversationMember> {
    int countMember(@Param("conversationId") Long conversationId,
                    @Param("userId") Long userId);

    int updateLastRead(@Param("conversationId") Long conversationId,
                       @Param("userId") Long userId,
                       @Param("lastReadMsgId") Long lastReadMsgId);

    Long peerId(@Param("conversationId") Long conversationId,
                @Param("selfId") Long selfId);
}
