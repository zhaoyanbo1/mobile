package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.EmergencyContactAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  紧急联系人 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IEmergencyContactAllService extends IService<EmergencyContactAll> {
        List<EmergencyContactAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<EmergencyContactAll> queryWrapper);
        List<EmergencyContactAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<EmergencyContactAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<EmergencyContactAll> queryWrapper);

        IPage pageNew(IPage<EmergencyContactAll> page, PageRequest pageRequest, LambdaQueryWrapper<EmergencyContactAll> queryWrapper);
        }
