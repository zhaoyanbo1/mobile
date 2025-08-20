package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.DietRecommendation;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> diet_recommendation Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface DietRecommendationStatisticsMapper extends BaseMapper<DietRecommendation> {

                        List<Object> diet_recommendation_creation_time_datetime_statistic_d9cff743_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> diet_recommendation_creation_time_datetime_statistic_24a31926_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> diet_recommendation_creation_time_datetime_statistic_6372a03f_count(@Param("ew") Wrapper queryWrapper);

}
