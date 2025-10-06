package com.kuafu.llm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.llm.entity.AiMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for AI conversation messages.
 */
@Mapper
public interface AiMessageMapper extends BaseMapper<AiMessage> {
}