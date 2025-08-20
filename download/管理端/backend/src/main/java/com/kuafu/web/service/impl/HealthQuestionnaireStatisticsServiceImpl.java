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

import com.kuafu.web.mapper.HealthQuestionnaireStatisticsMapper;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.service.IHealthQuestionnaireStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> health_questionnaire 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("HealthQuestionnaireStatistics")
public class HealthQuestionnaireStatisticsServiceImpl extends ServiceImpl<HealthQuestionnaireStatisticsMapper, HealthQuestionnaire> implements IHealthQuestionnaireStatisticsService {
    @Resource
    private HealthQuestionnaireStatisticsMapper healthQuestionnaireStaticMapper;

                        @Override
                        public List<Object> health_questionnaire_statistic_e3d3f9c6_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_statistic_e3d3f9c6_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_statistic_b3a63b11_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_statistic_b3a63b11_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_statistic_2228e20a_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_statistic_2228e20a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_creation_time_datetime_statistic_f09877db_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_creation_time_datetime_statistic_f09877db_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_creation_time_datetime_statistic_498a7150_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_creation_time_datetime_statistic_498a7150_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_update_time_datetime_statistic_a433a769_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_update_time_datetime_statistic_a433a769_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(queryWrapper);
                                }


                        @Override
                        public List<Object> health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(LambdaQueryWrapper queryWrapper) {
                                return healthQuestionnaireStaticMapper.health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(queryWrapper);
                                }


}
