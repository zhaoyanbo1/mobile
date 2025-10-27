package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.model.ImMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ImMessageMapper extends BaseMapper<ImMessage> {
    List<ImMessage> pageByConversation(@Param("conversationId") Long conversationId,
                                       @Param("beforeId") Long beforeId,
                                       @Param("limit") int limit);
}
