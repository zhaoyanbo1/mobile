package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.SystemSettingsAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  系统设置 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface ISystemSettingsAllService extends IService<SystemSettingsAll> {
        List<SystemSettingsAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper);
        List<SystemSettingsAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper);

        IPage pageNew(IPage<SystemSettingsAll> page, PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper);
        }
