package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.ReminderItemAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  提醒事项 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IReminderItemAllService extends IService<ReminderItemAll> {
        List<ReminderItemAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ReminderItemAll> queryWrapper);
        List<ReminderItemAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ReminderItemAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<ReminderItemAll> queryWrapper);

        IPage pageNew(IPage<ReminderItemAll> page, PageRequest pageRequest, LambdaQueryWrapper<ReminderItemAll> queryWrapper);
        }
