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
import com.kuafu.web.entity.SystemSettings;
import com.kuafu.web.service.ISystemSettingsStatisticsService;
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
@RequestMapping("/systemSettingsStatistics")

public class SystemSettingsStatisticsController  {

    private final ISystemSettingsStatisticsService systemSettingsStatisticsService;


    @PostMapping("system_settings_statistic_3692371e_count")
    public BaseResponse system_settings_statistic_3692371e_count(@RequestBody SystemSettings statisticVo) {
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( systemSettingsStatisticsService.system_settings_statistic_3692371e_count(queryWrapper));
    }
    @PostMapping("system_settings_statistic_278f24b9_count")
    public BaseResponse system_settings_statistic_278f24b9_count(@RequestBody SystemSettings statisticVo) {
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( systemSettingsStatisticsService.system_settings_statistic_278f24b9_count(queryWrapper));
    }
    @PostMapping("system_settings_statistic_80d1d596_count")
    public BaseResponse system_settings_statistic_80d1d596_count(@RequestBody SystemSettings statisticVo) {
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( systemSettingsStatisticsService.system_settings_statistic_80d1d596_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<SystemSettings> queryWrapper, SystemSettings statisticVo){
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(SystemSettings::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
            if(statisticVo.getReminderVolume() != null){
                queryWrapper.eq(SystemSettings::getReminderVolume, statisticVo.getReminderVolume());
            }
            if(statisticVo.getFontSize() != null){
                queryWrapper.eq(SystemSettings::getFontSize, statisticVo.getFontSize());
            }
            if(statisticVo.getQuestionnaireExported() != null){
                queryWrapper.eq(SystemSettings::getQuestionnaireExported, statisticVo.getQuestionnaireExported());
            }
    }



}
