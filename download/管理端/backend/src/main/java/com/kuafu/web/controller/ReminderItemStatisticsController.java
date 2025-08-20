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
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IReminderItemStatisticsService;
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
@RequestMapping("/reminderItemStatistics")

public class ReminderItemStatisticsController  {

    private final IReminderItemStatisticsService reminderItemStatisticsService;


    @PostMapping("reminder_item_statistic_faff4c4a_count")
    public BaseResponse reminder_item_statistic_faff4c4a_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_statistic_faff4c4a_count(queryWrapper));
    }
    @PostMapping("reminder_item_statistic_815d0c5c_count")
    public BaseResponse reminder_item_statistic_815d0c5c_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_statistic_815d0c5c_count(queryWrapper));
    }
    @PostMapping("reminder_item_statistic_ecf332eb_count")
    public BaseResponse reminder_item_statistic_ecf332eb_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_statistic_ecf332eb_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_type_enum_id_status_statistic_8519936e_count")
    public BaseResponse reminder_item_reminder_type_enum_id_status_statistic_8519936e_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_type_enum_id_status_statistic_8519936e_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_type_enum_id_status_statistic_f101bdc9_count")
    public BaseResponse reminder_item_reminder_type_enum_id_status_statistic_f101bdc9_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_type_enum_id_status_statistic_f101bdc9_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_type_enum_id_status_statistic_3156c6a1_count")
    public BaseResponse reminder_item_reminder_type_enum_id_status_statistic_3156c6a1_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_type_enum_id_status_statistic_3156c6a1_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_time_datetime_statistic_014c5c7f_count")
    public BaseResponse reminder_item_reminder_time_datetime_statistic_014c5c7f_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_time_datetime_statistic_014c5c7f_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_time_datetime_statistic_79feb197_count")
    public BaseResponse reminder_item_reminder_time_datetime_statistic_79feb197_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_time_datetime_statistic_79feb197_count(queryWrapper));
    }
    @PostMapping("reminder_item_reminder_time_datetime_statistic_9a48df92_count")
    public BaseResponse reminder_item_reminder_time_datetime_statistic_9a48df92_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_reminder_time_datetime_statistic_9a48df92_count(queryWrapper));
    }
    @PostMapping("reminder_item_creation_time_datetime_statistic_38c19f01_count")
    public BaseResponse reminder_item_creation_time_datetime_statistic_38c19f01_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_creation_time_datetime_statistic_38c19f01_count(queryWrapper));
    }
    @PostMapping("reminder_item_creation_time_datetime_statistic_b3b9e5e7_count")
    public BaseResponse reminder_item_creation_time_datetime_statistic_b3b9e5e7_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_creation_time_datetime_statistic_b3b9e5e7_count(queryWrapper));
    }
    @PostMapping("reminder_item_creation_time_datetime_statistic_4d13935a_count")
    public BaseResponse reminder_item_creation_time_datetime_statistic_4d13935a_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_creation_time_datetime_statistic_4d13935a_count(queryWrapper));
    }
    @PostMapping("reminder_item_update_time_datetime_statistic_221f7169_count")
    public BaseResponse reminder_item_update_time_datetime_statistic_221f7169_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_update_time_datetime_statistic_221f7169_count(queryWrapper));
    }
    @PostMapping("reminder_item_update_time_datetime_statistic_6dbe4d85_count")
    public BaseResponse reminder_item_update_time_datetime_statistic_6dbe4d85_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_update_time_datetime_statistic_6dbe4d85_count(queryWrapper));
    }
    @PostMapping("reminder_item_update_time_datetime_statistic_361033c3_count")
    public BaseResponse reminder_item_update_time_datetime_statistic_361033c3_count(@RequestBody ReminderItem statisticVo) {
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( reminderItemStatisticsService.reminder_item_update_time_datetime_statistic_361033c3_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ReminderItem> queryWrapper, ReminderItem statisticVo){
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(ReminderItem::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
            if(statisticVo.getReminderTypeEnumId() != null){
                queryWrapper.eq(ReminderItem::getReminderTypeEnumId, statisticVo.getReminderTypeEnumId());
            }
        if(StringUtils.isNotEmpty(statisticVo.getTitle())) {
                queryWrapper.eq(ReminderItem::getTitle, statisticVo.getTitle());
            }
        if(StringUtils.isNotEmpty(statisticVo.getDescription())) {
                queryWrapper.eq(ReminderItem::getDescription, statisticVo.getDescription());
            }
            if(statisticVo.getReminderTime() != null){
                queryWrapper.eq(ReminderItem::getReminderTime, statisticVo.getReminderTime());
            }
            if(statisticVo.getIsCompleted() != null){
                queryWrapper.eq(ReminderItem::getIsCompleted, statisticVo.getIsCompleted());
            }
        if(StringUtils.isNotEmpty(statisticVo.getMedicinePhotoResourceKey())) {
                queryWrapper.eq(ReminderItem::getMedicinePhotoResourceKey, statisticVo.getMedicinePhotoResourceKey());
            }
        if(StringUtils.isNotEmpty(statisticVo.getMedicineDosage())) {
                queryWrapper.eq(ReminderItem::getMedicineDosage, statisticVo.getMedicineDosage());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationLatitude())) {
                queryWrapper.eq(ReminderItem::getLocationLatitude, statisticVo.getLocationLatitude());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationLongitude())) {
                queryWrapper.eq(ReminderItem::getLocationLongitude, statisticVo.getLocationLongitude());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationAddress())) {
                queryWrapper.eq(ReminderItem::getLocationAddress, statisticVo.getLocationAddress());
            }
            if(statisticVo.getDietRecipeId() != null){
                queryWrapper.eq(ReminderItem::getDietRecipeId, statisticVo.getDietRecipeId());
            }
            if(statisticVo.getCreationTime() != null){
                queryWrapper.eq(ReminderItem::getCreationTime, statisticVo.getCreationTime());
            }
            if(statisticVo.getUpdateTime() != null){
                queryWrapper.eq(ReminderItem::getUpdateTime, statisticVo.getUpdateTime());
            }
    }



}
