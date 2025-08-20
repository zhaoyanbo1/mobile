package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.SystemSettings;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> system_settings Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface SystemSettingsStatisticsMapper extends BaseMapper<SystemSettings> {

                        List<Object> system_settings_statistic_3692371e_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> system_settings_statistic_278f24b9_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> system_settings_statistic_80d1d596_count(@Param("ew") Wrapper queryWrapper);

}
