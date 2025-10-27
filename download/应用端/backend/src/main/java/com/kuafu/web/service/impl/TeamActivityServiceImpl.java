package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.TeamActivity;
import com.kuafu.web.mapper.TeamActivityMapper;
import com.kuafu.web.service.ITeamActivityService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ITeamActivityService}.
 */
@Service("TeamActivity")
public class TeamActivityServiceImpl extends ServiceImpl<TeamActivityMapper, TeamActivity>
        implements ITeamActivityService {
}