package com.kuafu.web.controller;

import com.kuafu.common.util.poi.ExcelUtil;
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
import javax.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.SelectVO;
import com.kuafu.web.entity.HealthQuestionnaireAll;
import com.kuafu.web.service.IHealthQuestionnaireAllService;
import com.kuafu.web.vo.HealthQuestionnaireAllPageVO;

import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;


/**
 * <p> 健康问卷 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/healthQuestionnaireAll")
@Api(tags = {"健康问卷"})
public class HealthQuestionnaireAllController  {

    private final IHealthQuestionnaireAllService healthQuestionnaireAllService;

    @PostMapping("page")
    @ApiOperation("全字段分页")
    public BaseResponse page(@RequestBody HealthQuestionnaireAllPageVO pageVO){
        IPage<HealthQuestionnaireAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(pageVO.getLastLoginDate() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, pageVO.getLastLoginDate());
        }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(HealthQuestionnaireAll::getPhoneNumber, pageVO.getPhoneNumber());
        }
            if(pageVO.getRegistrationDate() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, pageVO.getRegistrationDate());
        }
            if(pageVO.getVersion() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getVersion, pageVO.getVersion());
        }
        if(StringUtils.isNotEmpty(pageVO.getChronicDisease())) {
            queryWrapper.eq(HealthQuestionnaireAll::getChronicDisease, pageVO.getChronicDisease());
        }
        if(StringUtils.isNotEmpty(pageVO.getCommonMedication())) {
            queryWrapper.eq(HealthQuestionnaireAll::getCommonMedication, pageVO.getCommonMedication());
        }
        if(StringUtils.isNotEmpty(pageVO.getExerciseFrequency())) {
            queryWrapper.eq(HealthQuestionnaireAll::getExerciseFrequency, pageVO.getExerciseFrequency());
        }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
            queryWrapper.eq(HealthQuestionnaireAll::getPassword, pageVO.getPassword());
        }
        if(StringUtils.isNotEmpty(pageVO.getAllergyHistory())) {
            queryWrapper.eq(HealthQuestionnaireAll::getAllergyHistory, pageVO.getAllergyHistory());
        }
        if(StringUtils.isNotEmpty(pageVO.getName())) {
            queryWrapper.like(HealthQuestionnaireAll::getName, pageVO.getName());
        }
        if(StringUtils.isNotEmpty(pageVO.getDietPreference())) {
            queryWrapper.eq(HealthQuestionnaireAll::getDietPreference, pageVO.getDietPreference());
        }
            if(pageVO.getCreationTime() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, pageVO.getCreationTime());
        }
        if(StringUtils.isNotEmpty(pageVO.getResidence())) {
            queryWrapper.eq(HealthQuestionnaireAll::getResidence, pageVO.getResidence());
        }
            if(pageVO.getUpdateTime() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, pageVO.getUpdateTime());
        }
            if(pageVO.getAge() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getAge, pageVO.getAge());
        }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {
            queryWrapper.like(HealthQuestionnaireAll::getUsername, pageVO.getUsername());
        }
        return ResultUtils.success(healthQuestionnaireAllService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("全字段列表")
    public BaseResponse list(@RequestBody HealthQuestionnaireAllPageVO vo){
        LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
                if(vo.getLastLoginDate() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, vo.getLastLoginDate());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(HealthQuestionnaireAll::getPhoneNumber, vo.getPhoneNumber());
        }
                if(vo.getRegistrationDate() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, vo.getRegistrationDate());
        }
                if(vo.getVersion() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getVersion, vo.getVersion());
        }
            if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
            queryWrapper.eq(HealthQuestionnaireAll::getChronicDisease, vo.getChronicDisease());
        }
            if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
            queryWrapper.eq(HealthQuestionnaireAll::getCommonMedication, vo.getCommonMedication());
        }
            if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
            queryWrapper.eq(HealthQuestionnaireAll::getExerciseFrequency, vo.getExerciseFrequency());
        }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
            queryWrapper.eq(HealthQuestionnaireAll::getPassword, vo.getPassword());
        }
            if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
            queryWrapper.eq(HealthQuestionnaireAll::getAllergyHistory, vo.getAllergyHistory());
        }
            if(StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like(HealthQuestionnaireAll::getName, vo.getName());
        }
            if(StringUtils.isNotEmpty(vo.getDietPreference())) {
            queryWrapper.eq(HealthQuestionnaireAll::getDietPreference, vo.getDietPreference());
        }
                if(vo.getCreationTime() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, vo.getCreationTime());
        }
            if(StringUtils.isNotEmpty(vo.getResidence())) {
            queryWrapper.eq(HealthQuestionnaireAll::getResidence, vo.getResidence());
        }
                if(vo.getUpdateTime() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, vo.getUpdateTime());
        }
                if(vo.getAge() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getAge, vo.getAge());
        }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
            queryWrapper.like(HealthQuestionnaireAll::getUsername, vo.getUsername());
        }
        return ResultUtils.success(healthQuestionnaireAllService.list(queryWrapper));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer healthQuestionnaireId) {
        HealthQuestionnaireAll entity = this.healthQuestionnaireAllService.getById(healthQuestionnaireId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


        private final IUserInfoService userInfoService;

        @GetMapping("get/user_info_list")
        @ApiOperation("查询下拉框的外键信息")
        public BaseResponse get_user_info_list() {
            List<UserInfo> list = this.userInfoService.list();
            final List<SelectVO> selectVoList = list.stream().map(p ->
                new SelectVO(p.getUserInfoId(), p.getPhoneNumber().toString()))
                        .collect(Collectors.toList());
            return ResultUtils.success(selectVoList);
        }


        /**
         * 导出excel
         */
        @PostMapping("/export")
        public void export(HttpServletResponse response, @RequestBody HealthQuestionnaireAllPageVO vo) {

            LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();

                    if(vo.getUserInfoUserInfoId1() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                    }
                    if(vo.getLastLoginDate() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, vo.getLastLoginDate());
                    }
                if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getPhoneNumber, vo.getPhoneNumber());
                    }
                    if(vo.getRegistrationDate() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, vo.getRegistrationDate());
                    }
                    if(vo.getVersion() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getVersion, vo.getVersion());
                    }
                if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getChronicDisease, vo.getChronicDisease());
                    }
                if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getCommonMedication, vo.getCommonMedication());
                    }
                if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getExerciseFrequency, vo.getExerciseFrequency());
                    }
                if(StringUtils.isNotEmpty(vo.getPassword())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getPassword, vo.getPassword());
                    }
                if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getAllergyHistory, vo.getAllergyHistory());
                    }
                if(StringUtils.isNotEmpty(vo.getName())) {
                                queryWrapper.like(HealthQuestionnaireAll::getName, vo.getName());
                    }
                if(StringUtils.isNotEmpty(vo.getDietPreference())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getDietPreference, vo.getDietPreference());
                    }
                    if(vo.getCreationTime() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, vo.getCreationTime());
                    }
                if(StringUtils.isNotEmpty(vo.getResidence())) {
                        queryWrapper.eq(HealthQuestionnaireAll::getResidence, vo.getResidence());
                    }
                    if(vo.getUpdateTime() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, vo.getUpdateTime());
                    }
                    if(vo.getAge() != null){
                        queryWrapper.eq(HealthQuestionnaireAll::getAge, vo.getAge());
                    }
                if(StringUtils.isNotEmpty(vo.getUsername())) {
                                queryWrapper.like(HealthQuestionnaireAll::getUsername, vo.getUsername());
                    }
            List<HealthQuestionnaireAll> list = healthQuestionnaireAllService.list(queryWrapper);
            ExcelUtil<HealthQuestionnaireAll> util = new ExcelUtil<>(HealthQuestionnaireAll.class);
            util.exportExcel(response, list, "数据");
        }


}
