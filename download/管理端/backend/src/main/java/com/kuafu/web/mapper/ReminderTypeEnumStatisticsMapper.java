package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.ReminderTypeEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> reminder_type_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface ReminderTypeEnumStatisticsMapper extends BaseMapper<ReminderTypeEnum> {

                        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(@Param("ew") Wrapper queryWrapper);

}
