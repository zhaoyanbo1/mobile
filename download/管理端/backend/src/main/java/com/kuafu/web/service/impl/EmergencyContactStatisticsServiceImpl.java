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

import com.kuafu.web.mapper.EmergencyContactStatisticsMapper;
import com.kuafu.web.entity.EmergencyContact;
import com.kuafu.web.service.IEmergencyContactStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> emergency_contact 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("EmergencyContactStatistics")
public class EmergencyContactStatisticsServiceImpl extends ServiceImpl<EmergencyContactStatisticsMapper, EmergencyContact> implements IEmergencyContactStatisticsService {
    @Resource
    private EmergencyContactStatisticsMapper emergencyContactStaticMapper;

                        @Override
                        public List<Object> emergency_contact_statistic_8ed9134a_count(LambdaQueryWrapper queryWrapper) {
                                return emergencyContactStaticMapper.emergency_contact_statistic_8ed9134a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> emergency_contact_statistic_e46df142_count(LambdaQueryWrapper queryWrapper) {
                                return emergencyContactStaticMapper.emergency_contact_statistic_e46df142_count(queryWrapper);
                                }


                        @Override
                        public List<Object> emergency_contact_statistic_efb671cc_count(LambdaQueryWrapper queryWrapper) {
                                return emergencyContactStaticMapper.emergency_contact_statistic_efb671cc_count(queryWrapper);
                                }


}
