package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  health_questionnaire 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IHealthQuestionnaireStatisticsService extends IService<HealthQuestionnaire> {
        List<Object> health_questionnaire_statistic_e3d3f9c6_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_statistic_b3a63b11_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_statistic_2228e20a_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_creation_time_datetime_statistic_f09877db_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_creation_time_datetime_statistic_498a7150_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_update_time_datetime_statistic_a433a769_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(LambdaQueryWrapper queryWrapper);

        List<Object> health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(LambdaQueryWrapper queryWrapper);

}
