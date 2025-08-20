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
import com.kuafu.web.entity.MedicineRecommendation;
import com.kuafu.web.service.IMedicineRecommendationStatisticsService;
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
@RequestMapping("/medicineRecommendationStatistics")

public class MedicineRecommendationStatisticsController  {

    private final IMedicineRecommendationStatisticsService medicineRecommendationStatisticsService;


    @PostMapping("medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count")
    public BaseResponse medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(@RequestBody MedicineRecommendation statisticVo) {
        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( medicineRecommendationStatisticsService.medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(queryWrapper));
    }
    @PostMapping("medicine_recommendation_creation_time_datetime_statistic_660c8953_count")
    public BaseResponse medicine_recommendation_creation_time_datetime_statistic_660c8953_count(@RequestBody MedicineRecommendation statisticVo) {
        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( medicineRecommendationStatisticsService.medicine_recommendation_creation_time_datetime_statistic_660c8953_count(queryWrapper));
    }
    @PostMapping("medicine_recommendation_creation_time_datetime_statistic_fb391654_count")
    public BaseResponse medicine_recommendation_creation_time_datetime_statistic_fb391654_count(@RequestBody MedicineRecommendation statisticVo) {
        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( medicineRecommendationStatisticsService.medicine_recommendation_creation_time_datetime_statistic_fb391654_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<MedicineRecommendation> queryWrapper, MedicineRecommendation statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTitle())) {
                queryWrapper.eq(MedicineRecommendation::getTitle, statisticVo.getTitle());
            }
        if(StringUtils.isNotEmpty(statisticVo.getUsageGuide())) {
                queryWrapper.eq(MedicineRecommendation::getUsageGuide, statisticVo.getUsageGuide());
            }
        if(StringUtils.isNotEmpty(statisticVo.getNearbyPharmacyInfo())) {
                queryWrapper.eq(MedicineRecommendation::getNearbyPharmacyInfo, statisticVo.getNearbyPharmacyInfo());
            }
            if(statisticVo.getCreationTime() != null){
                queryWrapper.eq(MedicineRecommendation::getCreationTime, statisticVo.getCreationTime());
            }
    }



}
