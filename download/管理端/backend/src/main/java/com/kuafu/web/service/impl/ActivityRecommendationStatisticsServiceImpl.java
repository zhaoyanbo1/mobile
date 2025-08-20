package com.kuafu.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafu.web.mapper.ActivityRecommendationStatisticsMapper;
import com.kuafu.web.entity.ActivityRecommendation;
import com.kuafu.web.service.IActivityRecommendationStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> activity_recommendation 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("ActivityRecommendationStatistics")
public class ActivityRecommendationStatisticsServiceImpl extends ServiceImpl<ActivityRecommendationStatisticsMapper, ActivityRecommendation> implements IActivityRecommendationStatisticsService {
    @Resource
    private ActivityRecommendationStatisticsMapper activityRecommendationStaticMapper;

                        @Override
                        public List<Object> activity_recommendation_activity_time_datetime_statistic_886e9306_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_activity_time_datetime_statistic_886e9306_count(queryWrapper);
                                }


                        @Override
                        public List<Object> activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(queryWrapper);
                                }


                        @Override
                        public List<Object> activity_recommendation_activity_time_datetime_statistic_470f405b_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_activity_time_datetime_statistic_470f405b_count(queryWrapper);
                                }


                        @Override
                        public List<Object> activity_recommendation_creation_time_datetime_statistic_08c7391b_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_creation_time_datetime_statistic_08c7391b_count(queryWrapper);
                                }


                        @Override
                        public List<Object> activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(queryWrapper);
                                }


                        @Override
                        public List<Object> activity_recommendation_creation_time_datetime_statistic_d08da80b_count(LambdaQueryWrapper queryWrapper) {
                                return activityRecommendationStaticMapper.activity_recommendation_creation_time_datetime_statistic_d08da80b_count(queryWrapper);
                                }


}
