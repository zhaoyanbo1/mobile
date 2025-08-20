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

import com.kuafu.web.mapper.DietRecommendationStatisticsMapper;
import com.kuafu.web.entity.DietRecommendation;
import com.kuafu.web.service.IDietRecommendationStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> diet_recommendation 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("DietRecommendationStatistics")
public class DietRecommendationStatisticsServiceImpl extends ServiceImpl<DietRecommendationStatisticsMapper, DietRecommendation> implements IDietRecommendationStatisticsService {
    @Resource
    private DietRecommendationStatisticsMapper dietRecommendationStaticMapper;

                        @Override
                        public List<Object> diet_recommendation_creation_time_datetime_statistic_d9cff743_count(LambdaQueryWrapper queryWrapper) {
                                return dietRecommendationStaticMapper.diet_recommendation_creation_time_datetime_statistic_d9cff743_count(queryWrapper);
                                }


                        @Override
                        public List<Object> diet_recommendation_creation_time_datetime_statistic_24a31926_count(LambdaQueryWrapper queryWrapper) {
                                return dietRecommendationStaticMapper.diet_recommendation_creation_time_datetime_statistic_24a31926_count(queryWrapper);
                                }


                        @Override
                        public List<Object> diet_recommendation_creation_time_datetime_statistic_6372a03f_count(LambdaQueryWrapper queryWrapper) {
                                return dietRecommendationStaticMapper.diet_recommendation_creation_time_datetime_statistic_6372a03f_count(queryWrapper);
                                }


}
