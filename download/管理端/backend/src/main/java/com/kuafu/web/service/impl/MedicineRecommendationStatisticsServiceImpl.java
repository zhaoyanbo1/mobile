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

import com.kuafu.web.mapper.MedicineRecommendationStatisticsMapper;
import com.kuafu.web.entity.MedicineRecommendation;
import com.kuafu.web.service.IMedicineRecommendationStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> medicine_recommendation 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("MedicineRecommendationStatistics")
public class MedicineRecommendationStatisticsServiceImpl extends ServiceImpl<MedicineRecommendationStatisticsMapper, MedicineRecommendation> implements IMedicineRecommendationStatisticsService {
    @Resource
    private MedicineRecommendationStatisticsMapper medicineRecommendationStaticMapper;

                        @Override
                        public List<Object> medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(LambdaQueryWrapper queryWrapper) {
                                return medicineRecommendationStaticMapper.medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> medicine_recommendation_creation_time_datetime_statistic_660c8953_count(LambdaQueryWrapper queryWrapper) {
                                return medicineRecommendationStaticMapper.medicine_recommendation_creation_time_datetime_statistic_660c8953_count(queryWrapper);
                                }


                        @Override
                        public List<Object> medicine_recommendation_creation_time_datetime_statistic_fb391654_count(LambdaQueryWrapper queryWrapper) {
                                return medicineRecommendationStaticMapper.medicine_recommendation_creation_time_datetime_statistic_fb391654_count(queryWrapper);
                                }


}
