package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.DietRecommendation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  diet_recommendation 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IDietRecommendationStatisticsService extends IService<DietRecommendation> {
        List<Object> diet_recommendation_creation_time_datetime_statistic_d9cff743_count(LambdaQueryWrapper queryWrapper);

        List<Object> diet_recommendation_creation_time_datetime_statistic_24a31926_count(LambdaQueryWrapper queryWrapper);

        List<Object> diet_recommendation_creation_time_datetime_statistic_6372a03f_count(LambdaQueryWrapper queryWrapper);

}
