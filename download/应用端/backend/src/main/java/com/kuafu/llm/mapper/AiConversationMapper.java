package com.kuafu.llm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.llm.entity.AiConversation;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for AI conversation persistence.
 */
@Mapper
public interface AiConversationMapper extends BaseMapper<AiConversation> {
}