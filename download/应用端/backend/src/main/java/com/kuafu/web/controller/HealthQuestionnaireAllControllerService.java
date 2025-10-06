package com.kuafu.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import java.io.Serializable;
import com.kuafu.common.login.IControllerService;

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
import org.apache.commons.lang3.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.kuafu.login.annotation.*;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.HealthQuestionnaireAll;
import com.kuafu.web.service.IHealthQuestionnaireAllService;
import com.kuafu.web.vo.HealthQuestionnaireAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;
import org.springframework.context.annotation.Lazy;





/**
 * <p> 健康问卷 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("HealthQuestionnaireAllControllerService")
public class HealthQuestionnaireAllControllerService implements IControllerService<HealthQuestionnaireAll> {


@Autowired
    private  IHealthQuestionnaireAllService healthQuestionnaireAllService;








    public BaseResponse page( HealthQuestionnaireAllPageVO pageVO){
        IPage<HealthQuestionnaireAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<HealthQuestionnaireAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("hq.health_questionnaire_id");
        LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, pageVO.getCreationTime());
            }
            if(pageVO.getHealthQuestionnaireId() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getHealthQuestionnaireId, pageVO.getHealthQuestionnaireId());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getAllergyHistory())) {
                queryWrapper.like(HealthQuestionnaireAll::getAllergyHistory, pageVO.getAllergyHistory());
            }
            if(pageVO.getLastLoginDate() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, pageVO.getLastLoginDate());
            }
            if(pageVO.getVersion() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getVersion, pageVO.getVersion());
            }
            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, pageVO.getUpdateTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(HealthQuestionnaireAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getCommonMedication())) {
                queryWrapper.like(HealthQuestionnaireAll::getCommonMedication, pageVO.getCommonMedication());
            }
        if(StringUtils.isNotEmpty(pageVO.getName())) {

                        String s_string = pageVO.getName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(HealthQuestionnaireAll::getName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getExerciseFrequency())) {
                queryWrapper.like(HealthQuestionnaireAll::getExerciseFrequency, pageVO.getExerciseFrequency());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(HealthQuestionnaireAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(pageVO.getResidence())) {
                queryWrapper.like(HealthQuestionnaireAll::getResidence, pageVO.getResidence());
            }
        if(StringUtils.isNotEmpty(pageVO.getChronicDisease())) {
                queryWrapper.like(HealthQuestionnaireAll::getChronicDisease, pageVO.getChronicDisease());
            }
            if(pageVO.getAge() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getAge, pageVO.getAge());
            }
        if(StringUtils.isNotEmpty(pageVO.getDietPreference())) {
                queryWrapper.like(HealthQuestionnaireAll::getDietPreference, pageVO.getDietPreference());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(HealthQuestionnaireAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(pageVO.getAdl() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getAdl, pageVO.getAdl());
        }
        if(pageVO.getMobilityOut() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getMobilityOut, pageVO.getMobilityOut());
        }
        if(pageVO.getFalls() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getFalls, pageVO.getFalls());
        }
        if(pageVO.getWeightLoss() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getWeightLoss, pageVO.getWeightLoss());
        }
        if(StringUtils.isNotEmpty(pageVO.getDiseases())) {
            queryWrapper.like(HealthQuestionnaireAll::getDiseases, pageVO.getDiseases());
        }
        if(pageVO.getPaMinutes() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getPaMinutes, pageVO.getPaMinutes());
        }
        if(pageVO.getPaWillingness() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getPaWillingness, pageVO.getPaWillingness());
        }
        if(pageVO.getFluVaccine() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getFluVaccine, pageVO.getFluVaccine());
        }
        if(pageVO.getPolypharmacy() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getPolypharmacy, pageVO.getPolypharmacy());
        }
        if(pageVO.getSocial() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getSocial, pageVO.getSocial());
        }
        if(pageVO.getFvServes() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getFvServes, pageVO.getFvServes());
        }
        if(pageVO.getTotalScore() != null){
            queryWrapper.eq(HealthQuestionnaireAll::getTotalScore, pageVO.getTotalScore());
        }
        if(StringUtils.isNotEmpty(pageVO.getRiskLevel())) {
            queryWrapper.like(HealthQuestionnaireAll::getRiskLevel, pageVO.getRiskLevel());
        }
        if(StringUtils.isNotEmpty(pageVO.getAnswersJson())) {
            queryWrapper.like(HealthQuestionnaireAll::getAnswersJson, pageVO.getAnswersJson());
        }

                    healthQuestionnaireAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });

        page.getRecords().forEach(item -> {
        });


            return ResultUtils.success(page);
        }

        public BaseResponse list(HealthQuestionnaireAllPageVO vo){
            LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getCreationTime() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, vo.getCreationTime());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getAllergyHistory, vo.getAllergyHistory());
                }
                if(vo.getLastLoginDate() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, vo.getLastLoginDate());
                }
                if(vo.getVersion() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getVersion, vo.getVersion());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, vo.getUpdateTime());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getPassword, vo.getPassword());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, vo.getRegistrationDate());
                }
            if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getCommonMedication, vo.getCommonMedication());
                }
            if(StringUtils.isNotEmpty(vo.getName())) {
                            queryWrapper.like(HealthQuestionnaireAll::getName, vo.getName());
                }
            if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getExerciseFrequency, vo.getExerciseFrequency());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getResidence())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getResidence, vo.getResidence());
                }
            if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getChronicDisease, vo.getChronicDisease());
                }
                if(vo.getAge() != null){
                    queryWrapper.eq(HealthQuestionnaireAll::getAge, vo.getAge());
                }
            if(StringUtils.isNotEmpty(vo.getDietPreference())) {
                    queryWrapper.eq(HealthQuestionnaireAll::getDietPreference, vo.getDietPreference());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(HealthQuestionnaireAll::getUsername, vo.getUsername());
                }
            if(vo.getAdl() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getAdl, vo.getAdl());
            }
            if(vo.getMobilityOut() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getMobilityOut, vo.getMobilityOut());
            }
            if(vo.getFalls() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getFalls, vo.getFalls());
            }
            if(vo.getWeightLoss() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getWeightLoss, vo.getWeightLoss());
            }
            if(StringUtils.isNotEmpty(vo.getDiseases())) {
                queryWrapper.eq(HealthQuestionnaireAll::getDiseases, vo.getDiseases());
            }
            if(vo.getPaMinutes() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getPaMinutes, vo.getPaMinutes());
            }
            if(vo.getPaWillingness() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getPaWillingness, vo.getPaWillingness());
            }
            if(vo.getFluVaccine() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getFluVaccine, vo.getFluVaccine());
            }
            if(vo.getPolypharmacy() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getPolypharmacy, vo.getPolypharmacy());
            }
            if(vo.getSocial() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getSocial, vo.getSocial());
            }
            if(vo.getFvServes() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getFvServes, vo.getFvServes());
            }
            if(vo.getTotalScore() != null){
                queryWrapper.eq(HealthQuestionnaireAll::getTotalScore, vo.getTotalScore());
            }
            if(StringUtils.isNotEmpty(vo.getRiskLevel())) {
                queryWrapper.eq(HealthQuestionnaireAll::getRiskLevel, vo.getRiskLevel());
            }
            if(StringUtils.isNotEmpty(vo.getAnswersJson())) {
                queryWrapper.eq(HealthQuestionnaireAll::getAnswersJson, vo.getAnswersJson());
            }
            return ResultUtils.success(healthQuestionnaireAllService.selectListNew(new PageRequest(),queryWrapper,false));
        }


            public BaseResponse get(HealthQuestionnaireAllPageVO vo) {


                LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getCreationTime() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getCreationTime, vo.getCreationTime());
                                }


                                if(vo.getHealthQuestionnaireId() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getHealthQuestionnaireId, vo.getHealthQuestionnaireId());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getAllergyHistory, vo.getAllergyHistory());
                                }


                                if(vo.getLastLoginDate() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getLastLoginDate, vo.getLastLoginDate());
                                }


                                if(vo.getVersion() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getVersion, vo.getVersion());
                                }


                                if(vo.getUpdateTime() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getUpdateTime, vo.getUpdateTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getPassword, vo.getPassword());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getCommonMedication, vo.getCommonMedication());
                                }


                            if(StringUtils.isNotEmpty(vo.getName())) {
                                            queryWrapper.like(HealthQuestionnaireAll::getName, vo.getName());
                                }


                            if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getExerciseFrequency, vo.getExerciseFrequency());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                            if(StringUtils.isNotEmpty(vo.getResidence())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getResidence, vo.getResidence());
                                }


                            if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getChronicDisease, vo.getChronicDisease());
                                }


                                if(vo.getAge() != null){
                                    queryWrapper.eq(HealthQuestionnaireAll::getAge, vo.getAge());
                                }


                            if(StringUtils.isNotEmpty(vo.getDietPreference())) {
                                    queryWrapper.eq(HealthQuestionnaireAll::getDietPreference, vo.getDietPreference());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(HealthQuestionnaireAll::getUsername, vo.getUsername());
                                }

                // HealthQuestionnaireAll entity = this.healthQuestionnaireAllService.getById(healthQuestionnaireId);
                    final Page<HealthQuestionnaireAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    healthQuestionnaireAllService.pageNew(page, vo, queryWrapper);
                    final List<HealthQuestionnaireAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public HealthQuestionnaireAll getById(Serializable healthQuestionnaireId) {

                    LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(HealthQuestionnaireAll::getHealthQuestionnaireId, healthQuestionnaireId);

                    final Page<HealthQuestionnaireAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    healthQuestionnaireAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<HealthQuestionnaireAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }
