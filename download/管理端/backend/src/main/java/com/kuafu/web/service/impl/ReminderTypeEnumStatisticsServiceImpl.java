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

import com.kuafu.web.mapper.ReminderTypeEnumStatisticsMapper;
import com.kuafu.web.entity.ReminderTypeEnum;
import com.kuafu.web.service.IReminderTypeEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> reminder_type_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("ReminderTypeEnumStatistics")
public class ReminderTypeEnumStatisticsServiceImpl extends ServiceImpl<ReminderTypeEnumStatisticsMapper, ReminderTypeEnum> implements IReminderTypeEnumStatisticsService {
    @Resource
    private ReminderTypeEnumStatisticsMapper reminderTypeEnumStaticMapper;

                        @Override
                        public List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(LambdaQueryWrapper queryWrapper) {
                                return reminderTypeEnumStaticMapper.reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(LambdaQueryWrapper queryWrapper) {
                                return reminderTypeEnumStaticMapper.reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(LambdaQueryWrapper queryWrapper) {
                                return reminderTypeEnumStaticMapper.reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(queryWrapper);
                                }


}
