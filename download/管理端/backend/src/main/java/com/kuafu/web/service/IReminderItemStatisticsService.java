package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.ReminderItem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  reminder_item 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IReminderItemStatisticsService extends IService<ReminderItem> {
        List<Object> reminder_item_statistic_faff4c4a_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_statistic_815d0c5c_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_statistic_ecf332eb_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_type_enum_id_status_statistic_8519936e_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_type_enum_id_status_statistic_f101bdc9_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_type_enum_id_status_statistic_3156c6a1_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_time_datetime_statistic_014c5c7f_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_time_datetime_statistic_79feb197_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_reminder_time_datetime_statistic_9a48df92_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_creation_time_datetime_statistic_38c19f01_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_creation_time_datetime_statistic_b3b9e5e7_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_creation_time_datetime_statistic_4d13935a_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_update_time_datetime_statistic_221f7169_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_update_time_datetime_statistic_6dbe4d85_count(LambdaQueryWrapper queryWrapper);

        List<Object> reminder_item_update_time_datetime_statistic_361033c3_count(LambdaQueryWrapper queryWrapper);

}
