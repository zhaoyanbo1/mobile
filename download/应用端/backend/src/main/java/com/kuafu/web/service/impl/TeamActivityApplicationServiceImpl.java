package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.TeamActivityApplication;
import com.kuafu.web.mapper.TeamActivityApplicationMapper;
import com.kuafu.web.service.ITeamActivityApplicationService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ITeamActivityApplicationService}.
 */
@Service("TeamActivityApplication")
public class TeamActivityApplicationServiceImpl extends ServiceImpl<TeamActivityApplicationMapper, TeamActivityApplication>
        implements ITeamActivityApplicationService {
}