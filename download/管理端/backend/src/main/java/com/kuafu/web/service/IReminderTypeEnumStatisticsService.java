package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.ReminderTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  reminder_type_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IReminderTypeEnumStatisticsService extends IService<ReminderTypeEnum> {
        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(LambdaQueryWrapper queryWrapper);

}
