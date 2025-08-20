package com.kuafu.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.EmergencyContact;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> emergency_contact Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface EmergencyContactStatisticsMapper extends BaseMapper<EmergencyContact> {

                        List<Object> emergency_contact_statistic_8ed9134a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> emergency_contact_statistic_e46df142_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> emergency_contact_statistic_efb671cc_count(@Param("ew") Wrapper queryWrapper);

}
