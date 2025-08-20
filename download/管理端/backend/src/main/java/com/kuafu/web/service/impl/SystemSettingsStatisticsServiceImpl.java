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

import com.kuafu.web.mapper.SystemSettingsStatisticsMapper;
import com.kuafu.web.entity.SystemSettings;
import com.kuafu.web.service.ISystemSettingsStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> system_settings 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("SystemSettingsStatistics")
public class SystemSettingsStatisticsServiceImpl extends ServiceImpl<SystemSettingsStatisticsMapper, SystemSettings> implements ISystemSettingsStatisticsService {
    @Resource
    private SystemSettingsStatisticsMapper systemSettingsStaticMapper;

                        @Override
                        public List<Object> system_settings_statistic_3692371e_count(LambdaQueryWrapper queryWrapper) {
                                return systemSettingsStaticMapper.system_settings_statistic_3692371e_count(queryWrapper);
                                }


                        @Override
                        public List<Object> system_settings_statistic_278f24b9_count(LambdaQueryWrapper queryWrapper) {
                                return systemSettingsStaticMapper.system_settings_statistic_278f24b9_count(queryWrapper);
                                }


                        @Override
                        public List<Object> system_settings_statistic_80d1d596_count(LambdaQueryWrapper queryWrapper) {
                                return systemSettingsStaticMapper.system_settings_statistic_80d1d596_count(queryWrapper);
                                }


}
