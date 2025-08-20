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
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.service.IHealthQuestionnaireStatisticsService;
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
@RequestMapping("/healthQuestionnaireStatistics")

public class HealthQuestionnaireStatisticsController  {

    private final IHealthQuestionnaireStatisticsService healthQuestionnaireStatisticsService;


    @PostMapping("health_questionnaire_statistic_e3d3f9c6_count")
    public BaseResponse health_questionnaire_statistic_e3d3f9c6_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_statistic_e3d3f9c6_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_statistic_b3a63b11_count")
    public BaseResponse health_questionnaire_statistic_b3a63b11_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_statistic_b3a63b11_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_statistic_2228e20a_count")
    public BaseResponse health_questionnaire_statistic_2228e20a_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_statistic_2228e20a_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_creation_time_datetime_statistic_88df0f9b_count")
    public BaseResponse health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_creation_time_datetime_statistic_f09877db_count")
    public BaseResponse health_questionnaire_creation_time_datetime_statistic_f09877db_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_creation_time_datetime_statistic_f09877db_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_creation_time_datetime_statistic_498a7150_count")
    public BaseResponse health_questionnaire_creation_time_datetime_statistic_498a7150_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_creation_time_datetime_statistic_498a7150_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_update_time_datetime_statistic_a433a769_count")
    public BaseResponse health_questionnaire_update_time_datetime_statistic_a433a769_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_update_time_datetime_statistic_a433a769_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_update_time_datetime_statistic_b1d5cba1_count")
    public BaseResponse health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(queryWrapper));
    }
    @PostMapping("health_questionnaire_update_time_datetime_statistic_9bad8d9e_count")
    public BaseResponse health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(@RequestBody HealthQuestionnaire statisticVo) {
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( healthQuestionnaireStatisticsService.health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<HealthQuestionnaire> queryWrapper, HealthQuestionnaire statisticVo){
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getName())) {
                        queryWrapper.like(HealthQuestionnaire::getName, statisticVo.getName());
            }
            if(statisticVo.getAge() != null){
                queryWrapper.eq(HealthQuestionnaire::getAge, statisticVo.getAge());
            }
        if(StringUtils.isNotEmpty(statisticVo.getResidence())) {
                queryWrapper.eq(HealthQuestionnaire::getResidence, statisticVo.getResidence());
            }
        if(StringUtils.isNotEmpty(statisticVo.getChronicDisease())) {
                queryWrapper.eq(HealthQuestionnaire::getChronicDisease, statisticVo.getChronicDisease());
            }
        if(StringUtils.isNotEmpty(statisticVo.getAllergyHistory())) {
                queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, statisticVo.getAllergyHistory());
            }
        if(StringUtils.isNotEmpty(statisticVo.getCommonMedication())) {
                queryWrapper.eq(HealthQuestionnaire::getCommonMedication, statisticVo.getCommonMedication());
            }
        if(StringUtils.isNotEmpty(statisticVo.getDietPreference())) {
                queryWrapper.eq(HealthQuestionnaire::getDietPreference, statisticVo.getDietPreference());
            }
        if(StringUtils.isNotEmpty(statisticVo.getExerciseFrequency())) {
                queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, statisticVo.getExerciseFrequency());
            }
            if(statisticVo.getCreationTime() != null){
                queryWrapper.eq(HealthQuestionnaire::getCreationTime, statisticVo.getCreationTime());
            }
            if(statisticVo.getUpdateTime() != null){
                queryWrapper.eq(HealthQuestionnaire::getUpdateTime, statisticVo.getUpdateTime());
            }
            if(statisticVo.getVersion() != null){
                queryWrapper.eq(HealthQuestionnaire::getVersion, statisticVo.getVersion());
            }
    }



}
