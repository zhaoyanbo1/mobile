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
import com.kuafu.web.entity.ActivityRecommendation;
import com.kuafu.web.service.IActivityRecommendationStatisticsService;
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
@RequestMapping("/activityRecommendationStatistics")

public class ActivityRecommendationStatisticsController  {

    private final IActivityRecommendationStatisticsService activityRecommendationStatisticsService;


    @PostMapping("activity_recommendation_activity_time_datetime_statistic_886e9306_count")
    public BaseResponse activity_recommendation_activity_time_datetime_statistic_886e9306_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_activity_time_datetime_statistic_886e9306_count(queryWrapper));
    }
    @PostMapping("activity_recommendation_activity_time_datetime_statistic_d3b0c288_count")
    public BaseResponse activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(queryWrapper));
    }
    @PostMapping("activity_recommendation_activity_time_datetime_statistic_470f405b_count")
    public BaseResponse activity_recommendation_activity_time_datetime_statistic_470f405b_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_activity_time_datetime_statistic_470f405b_count(queryWrapper));
    }
    @PostMapping("activity_recommendation_creation_time_datetime_statistic_08c7391b_count")
    public BaseResponse activity_recommendation_creation_time_datetime_statistic_08c7391b_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_creation_time_datetime_statistic_08c7391b_count(queryWrapper));
    }
    @PostMapping("activity_recommendation_creation_time_datetime_statistic_ef3081ce_count")
    public BaseResponse activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(queryWrapper));
    }
    @PostMapping("activity_recommendation_creation_time_datetime_statistic_d08da80b_count")
    public BaseResponse activity_recommendation_creation_time_datetime_statistic_d08da80b_count(@RequestBody ActivityRecommendation statisticVo) {
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( activityRecommendationStatisticsService.activity_recommendation_creation_time_datetime_statistic_d08da80b_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ActivityRecommendation> queryWrapper, ActivityRecommendation statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTitle())) {
                queryWrapper.eq(ActivityRecommendation::getTitle, statisticVo.getTitle());
            }
            if(statisticVo.getActivityTime() != null){
                queryWrapper.eq(ActivityRecommendation::getActivityTime, statisticVo.getActivityTime());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationLatitude())) {
                queryWrapper.eq(ActivityRecommendation::getLocationLatitude, statisticVo.getLocationLatitude());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationLongitude())) {
                queryWrapper.eq(ActivityRecommendation::getLocationLongitude, statisticVo.getLocationLongitude());
            }
        if(StringUtils.isNotEmpty(statisticVo.getLocationAddress())) {
                queryWrapper.eq(ActivityRecommendation::getLocationAddress, statisticVo.getLocationAddress());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSuitablePeople())) {
                queryWrapper.eq(ActivityRecommendation::getSuitablePeople, statisticVo.getSuitablePeople());
            }
            if(statisticVo.getCreationTime() != null){
                queryWrapper.eq(ActivityRecommendation::getCreationTime, statisticVo.getCreationTime());
            }
    }



}
