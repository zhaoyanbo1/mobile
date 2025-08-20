package com.kuafu.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.EmergencyContact;
import com.kuafu.web.service.IEmergencyContactStatisticsService;
import com.kuafu.web.entity.SelectVO;

import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;





/**

 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/emergencyContactStatistics")

public class EmergencyContactStatisticsController  {

    private final IEmergencyContactStatisticsService emergencyContactStatisticsService;


    @PostMapping("emergency_contact_statistic_8ed9134a_count")
    public BaseResponse emergency_contact_statistic_8ed9134a_count(@RequestBody EmergencyContact statisticVo) {
        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( emergencyContactStatisticsService.emergency_contact_statistic_8ed9134a_count(queryWrapper));
    }
    @PostMapping("emergency_contact_statistic_e46df142_count")
    public BaseResponse emergency_contact_statistic_e46df142_count(@RequestBody EmergencyContact statisticVo) {
        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( emergencyContactStatisticsService.emergency_contact_statistic_e46df142_count(queryWrapper));
    }
    @PostMapping("emergency_contact_statistic_efb671cc_count")
    public BaseResponse emergency_contact_statistic_efb671cc_count(@RequestBody EmergencyContact statisticVo) {
        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( emergencyContactStatisticsService.emergency_contact_statistic_efb671cc_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<EmergencyContact> queryWrapper, EmergencyContact statisticVo){
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getName())) {
                        queryWrapper.like(EmergencyContact::getName, statisticVo.getName());
            }
        if(StringUtils.isNotEmpty(statisticVo.getPhoneNumber())) {
                queryWrapper.eq(EmergencyContact::getPhoneNumber, statisticVo.getPhoneNumber());
            }
    }



}
