package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.ReminderItem;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> reminder_item Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface ReminderItemStatisticsMapper extends BaseMapper<ReminderItem> {

                        List<Object> reminder_item_statistic_faff4c4a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_statistic_815d0c5c_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_statistic_ecf332eb_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_type_enum_id_status_statistic_8519936e_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_type_enum_id_status_statistic_f101bdc9_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_type_enum_id_status_statistic_3156c6a1_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_time_datetime_statistic_014c5c7f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_time_datetime_statistic_79feb197_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_reminder_time_datetime_statistic_9a48df92_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_creation_time_datetime_statistic_38c19f01_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_creation_time_datetime_statistic_b3b9e5e7_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_creation_time_datetime_statistic_4d13935a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_update_time_datetime_statistic_221f7169_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_update_time_datetime_statistic_6dbe4d85_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_item_update_time_datetime_statistic_361033c3_count(@Param("ew") Wrapper queryWrapper);

}
