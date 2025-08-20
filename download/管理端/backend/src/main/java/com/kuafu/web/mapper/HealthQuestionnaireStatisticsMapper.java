package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.HealthQuestionnaire;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> health_questionnaire Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface HealthQuestionnaireStatisticsMapper extends BaseMapper<HealthQuestionnaire> {

                        List<Object> health_questionnaire_statistic_e3d3f9c6_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_statistic_b3a63b11_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_statistic_2228e20a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_creation_time_datetime_statistic_f09877db_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_creation_time_datetime_statistic_498a7150_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_update_time_datetime_statistic_a433a769_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(@Param("ew") Wrapper queryWrapper);

}
