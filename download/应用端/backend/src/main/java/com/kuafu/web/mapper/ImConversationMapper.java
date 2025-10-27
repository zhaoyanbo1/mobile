package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.model.ImConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImConversationMapper extends BaseMapper<ImConversation> {
    int updateLastMessage(@Param("conversationId") Long conversationId,
                          @Param("lastMessageId") Long lastMessageId);
}
