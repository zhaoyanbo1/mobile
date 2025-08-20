package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.SystemSettings;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  system_settings 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface ISystemSettingsStatisticsService extends IService<SystemSettings> {
        List<Object> system_settings_statistic_3692371e_count(LambdaQueryWrapper queryWrapper);

        List<Object> system_settings_statistic_278f24b9_count(LambdaQueryWrapper queryWrapper);

        List<Object> system_settings_statistic_80d1d596_count(LambdaQueryWrapper queryWrapper);

}
