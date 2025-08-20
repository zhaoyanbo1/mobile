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
import com.kuafu.web.entity.ReminderTypeEnum;
import com.kuafu.web.service.IReminderTypeEnumStatisticsService;
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
@RequestMapping("/reminderTypeEnumStatistics")

public class ReminderTypeEnumStatisticsController  {

    private final IReminderTypeEnumStatisticsService reminderTypeEnumStatisticsService;


    @PostMapping("reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count")
    public BaseResponse reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(@RequestBody ReminderTypeEnum statisticVo) {
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderTypeEnumStatisticsService.reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(queryWrapper));
    }
    @PostMapping("reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count")
    public BaseResponse reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(@RequestBody ReminderTypeEnum statisticVo) {
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderTypeEnumStatisticsService.reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(queryWrapper));
    }
    @PostMapping("reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count")
    public BaseResponse reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(@RequestBody ReminderTypeEnum statisticVo) {
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderTypeEnumStatisticsService.reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ReminderTypeEnum> queryWrapper, ReminderTypeEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTypeName())) {
                        queryWrapper.like(ReminderTypeEnum::getTypeName, statisticVo.getTypeName());
            }
    }



}
