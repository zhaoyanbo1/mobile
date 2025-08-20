package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.ActivityRecommendation;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> activity_recommendation Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface ActivityRecommendationStatisticsMapper extends BaseMapper<ActivityRecommendation> {

                        List<Object> activity_recommendation_activity_time_datetime_statistic_886e9306_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> activity_recommendation_activity_time_datetime_statistic_470f405b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> activity_recommendation_creation_time_datetime_statistic_08c7391b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> activity_recommendation_creation_time_datetime_statistic_d08da80b_count(@Param("ew") Wrapper queryWrapper);

}
