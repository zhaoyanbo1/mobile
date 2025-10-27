package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.TeamActivityParticipant;
import com.kuafu.web.mapper.TeamActivityParticipantMapper;
import com.kuafu.web.service.ITeamActivityParticipantService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ITeamActivityParticipantService}.
 */
@Service("TeamActivityParticipant")
public class TeamActivityParticipantServiceImpl extends ServiceImpl<TeamActivityParticipantMapper, TeamActivityParticipant>
        implements ITeamActivityParticipantService {
}