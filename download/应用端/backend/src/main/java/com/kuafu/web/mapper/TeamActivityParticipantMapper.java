package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.TeamActivityParticipant;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for activity participants.
 */
@Mapper
public interface TeamActivityParticipantMapper extends BaseMapper<TeamActivityParticipant> {
}