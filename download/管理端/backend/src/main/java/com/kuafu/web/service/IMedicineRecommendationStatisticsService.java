package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.MedicineRecommendation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  medicine_recommendation 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IMedicineRecommendationStatisticsService extends IService<MedicineRecommendation> {
        List<Object> medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(LambdaQueryWrapper queryWrapper);

        List<Object> medicine_recommendation_creation_time_datetime_statistic_660c8953_count(LambdaQueryWrapper queryWrapper);

        List<Object> medicine_recommendation_creation_time_datetime_statistic_fb391654_count(LambdaQueryWrapper queryWrapper);

}
