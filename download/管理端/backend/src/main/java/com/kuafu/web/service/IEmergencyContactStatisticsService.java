package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.EmergencyContact;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  emergency_contact 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IEmergencyContactStatisticsService extends IService<EmergencyContact> {
        List<Object> emergency_contact_statistic_8ed9134a_count(LambdaQueryWrapper queryWrapper);

        List<Object> emergency_contact_statistic_e46df142_count(LambdaQueryWrapper queryWrapper);

        List<Object> emergency_contact_statistic_efb671cc_count(LambdaQueryWrapper queryWrapper);

}
