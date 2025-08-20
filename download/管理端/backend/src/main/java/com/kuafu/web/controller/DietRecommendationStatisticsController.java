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
import com.kuafu.web.entity.DietRecommendation;
import com.kuafu.web.service.IDietRecommendationStatisticsService;
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
@RequestMapping("/dietRecommendationStatistics")

public class DietRecommendationStatisticsController  {

    private final IDietRecommendationStatisticsService dietRecommendationStatisticsService;


    @PostMapping("diet_recommendation_creation_time_datetime_statistic_d9cff743_count")
    public BaseResponse diet_recommendation_creation_time_datetime_statistic_d9cff743_count(@RequestBody DietRecommendation statisticVo) {
        LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( dietRecommendationStatisticsService.diet_recommendation_creation_time_datetime_statistic_d9cff743_count(queryWrapper));
    }
    @PostMapping("diet_recommendation_creation_time_datetime_statistic_24a31926_count")
    public BaseResponse diet_recommendation_creation_time_datetime_statistic_24a31926_count(@RequestBody DietRecommendation statisticVo) {
        LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( dietRecommendationStatisticsService.diet_recommendation_creation_time_datetime_statistic_24a31926_count(queryWrapper));
    }
    @PostMapping("diet_recommendation_creation_time_datetime_statistic_6372a03f_count")
    public BaseResponse diet_recommendation_creation_time_datetime_statistic_6372a03f_count(@RequestBody DietRecommendation statisticVo) {
        LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( dietRecommendationStatisticsService.diet_recommendation_creation_time_datetime_statistic_6372a03f_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<DietRecommendation> queryWrapper, DietRecommendation statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTitle())) {
                queryWrapper.eq(DietRecommendation::getTitle, statisticVo.getTitle());
            }
        if(StringUtils.isNotEmpty(statisticVo.getDifficulty())) {
                queryWrapper.eq(DietRecommendation::getDifficulty, statisticVo.getDifficulty());
            }
        if(StringUtils.isNotEmpty(statisticVo.getRequiredTime())) {
                queryWrapper.eq(DietRecommendation::getRequiredTime, statisticVo.getRequiredTime());
            }
            if(statisticVo.getCreationTime() != null){
                queryWrapper.eq(DietRecommendation::getCreationTime, statisticVo.getCreationTime());
            }
    }



}
