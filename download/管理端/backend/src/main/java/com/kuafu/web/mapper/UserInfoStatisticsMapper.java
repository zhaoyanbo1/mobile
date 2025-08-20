package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.UserInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> user_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface UserInfoStatisticsMapper extends BaseMapper<UserInfo> {

                        List<Object> user_info_registration_date_datetime_statistic_e1bf3d5d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_registration_date_datetime_statistic_683a2401_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_registration_date_datetime_statistic_1e61d94d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_last_login_date_datetime_statistic_10ddf510_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_last_login_date_datetime_statistic_65a6ff33_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_last_login_date_datetime_statistic_d6db3a40_count(@Param("ew") Wrapper queryWrapper);

}
