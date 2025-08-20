package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  user_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IUserInfoStatisticsService extends IService<UserInfo> {
        List<Object> user_info_registration_date_datetime_statistic_e1bf3d5d_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_registration_date_datetime_statistic_683a2401_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_registration_date_datetime_statistic_1e61d94d_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_last_login_date_datetime_statistic_10ddf510_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_last_login_date_datetime_statistic_65a6ff33_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_last_login_date_datetime_statistic_d6db3a40_count(LambdaQueryWrapper queryWrapper);

}
