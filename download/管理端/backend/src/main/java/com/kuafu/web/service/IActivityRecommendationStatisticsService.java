package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.ActivityRecommendation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  activity_recommendation 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IActivityRecommendationStatisticsService extends IService<ActivityRecommendation> {
        List<Object> activity_recommendation_activity_time_datetime_statistic_886e9306_count(LambdaQueryWrapper queryWrapper);

        List<Object> activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(LambdaQueryWrapper queryWrapper);

        List<Object> activity_recommendation_activity_time_datetime_statistic_470f405b_count(LambdaQueryWrapper queryWrapper);

        List<Object> activity_recommendation_creation_time_datetime_statistic_08c7391b_count(LambdaQueryWrapper queryWrapper);

        List<Object> activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(LambdaQueryWrapper queryWrapper);

        List<Object> activity_recommendation_creation_time_datetime_statistic_d08da80b_count(LambdaQueryWrapper queryWrapper);

}
