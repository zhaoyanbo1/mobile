package com.kuafu.web.controller;

import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.event.EventVo;
import com.kuafu.common.event.MyEventService;
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
import org.springframework.transaction.support.TransactionTemplate;
import com.kuafu.common.sensitive.SensitiveFilter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.service.IHealthQuestionnaireService;
import com.kuafu.web.vo.HealthQuestionnairePageVO;
import com.kuafu.web.vo.HealthQuestionnaireVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 健康问卷 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("HealthQuestionnaireControllerService")
@RequiredArgsConstructor
public class HealthQuestionnaireControllerService  {

    private final IHealthQuestionnaireService healthQuestionnaireService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( HealthQuestionnairePageVO pageVO){
        IPage<HealthQuestionnaire> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<HealthQuestionnaire> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(HealthQuestionnaire::getHealthQuestionnaireId);

            if(pageVO.getHealthQuestionnaireId() != null){
                queryWrapper.eq(HealthQuestionnaire::getHealthQuestionnaireId, pageVO.getHealthQuestionnaireId());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getName())) {
//                        queryWrapper.like(HealthQuestionnaire::getName, pageVO.getName());
                        String s_string = pageVO.getName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(HealthQuestionnaire::getName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


            if(pageVO.getAge() != null){
                queryWrapper.eq(HealthQuestionnaire::getAge, pageVO.getAge());
            }


        if(StringUtils.isNotEmpty(pageVO.getResidence())) {
                queryWrapper.eq(HealthQuestionnaire::getResidence, pageVO.getResidence());
            }


        if(StringUtils.isNotEmpty(pageVO.getChronicDisease())) {
                queryWrapper.eq(HealthQuestionnaire::getChronicDisease, pageVO.getChronicDisease());
            }


        if(StringUtils.isNotEmpty(pageVO.getAllergyHistory())) {
                queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, pageVO.getAllergyHistory());
            }


        if(StringUtils.isNotEmpty(pageVO.getCommonMedication())) {
                queryWrapper.eq(HealthQuestionnaire::getCommonMedication, pageVO.getCommonMedication());
            }


        if(StringUtils.isNotEmpty(pageVO.getDietPreference())) {
                queryWrapper.eq(HealthQuestionnaire::getDietPreference, pageVO.getDietPreference());
            }


        if(StringUtils.isNotEmpty(pageVO.getExerciseFrequency())) {
                queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, pageVO.getExerciseFrequency());
            }


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(HealthQuestionnaire::getCreationTime, pageVO.getCreationTime());
            }


            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(HealthQuestionnaire::getUpdateTime, pageVO.getUpdateTime());
            }


            if(pageVO.getVersion() != null){
                queryWrapper.eq(HealthQuestionnaire::getVersion, pageVO.getVersion());
            }

            return ResultUtils.success(healthQuestionnaireService.page(page, queryWrapper));
        }


        public BaseResponse list(  HealthQuestionnaireVO vo){
            LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getName())) {
                            queryWrapper.like(HealthQuestionnaire::getName, vo.getName());
                }
                if(vo.getAge() != null){
                    queryWrapper.eq(HealthQuestionnaire::getAge, vo.getAge());
                }
            if(StringUtils.isNotEmpty(vo.getResidence())) {
                    queryWrapper.eq(HealthQuestionnaire::getResidence, vo.getResidence());
                }
            if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
                    queryWrapper.eq(HealthQuestionnaire::getChronicDisease, vo.getChronicDisease());
                }
            if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
                    queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, vo.getAllergyHistory());
                }
            if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
                    queryWrapper.eq(HealthQuestionnaire::getCommonMedication, vo.getCommonMedication());
                }
            if(StringUtils.isNotEmpty(vo.getDietPreference())) {
                    queryWrapper.eq(HealthQuestionnaire::getDietPreference, vo.getDietPreference());
                }
            if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
                    queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, vo.getExerciseFrequency());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(HealthQuestionnaire::getCreationTime, vo.getCreationTime());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(HealthQuestionnaire::getUpdateTime, vo.getUpdateTime());
                }
                if(vo.getVersion() != null){
                    queryWrapper.eq(HealthQuestionnaire::getVersion, vo.getVersion());
                }
                return ResultUtils.success(healthQuestionnaireService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  HealthQuestionnaireVO vo) {
                HealthQuestionnaire entity =  HealthQuestionnaire.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .age(vo.getAge())
                    .residence(vo.getResidence())
                    .chronicDisease(vo.getChronicDisease())
                    .allergyHistory(vo.getAllergyHistory())
                    .commonMedication(vo.getCommonMedication())
                    .dietPreference(vo.getDietPreference())
                    .exerciseFrequency(vo.getExerciseFrequency())
                    .creationTime(vo.getCreationTime())
                    .version(vo.getVersion())
                .build();








                boolean flag =this.healthQuestionnaireService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("health_questionnaire").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getHealthQuestionnaireId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( HealthQuestionnaireVO vo) {
                HealthQuestionnaire entity =  HealthQuestionnaire.builder()
                    .healthQuestionnaireId(vo.getHealthQuestionnaireId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .age(vo.getAge())
                    .residence(vo.getResidence())
                    .chronicDisease(vo.getChronicDisease())
                    .allergyHistory(vo.getAllergyHistory())
                    .commonMedication(vo.getCommonMedication())
                    .dietPreference(vo.getDietPreference())
                    .exerciseFrequency(vo.getExerciseFrequency())
                    .creationTime(vo.getCreationTime())
                    .version(vo.getVersion())
                .build();



                        boolean flag = this.healthQuestionnaireService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getHealthQuestionnaireId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( HealthQuestionnaireVO vo ) {
                HealthQuestionnaire entity = this.healthQuestionnaireService.getById(vo.getHealthQuestionnaireId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( HealthQuestionnaireVO vo) {
                boolean flag = this.healthQuestionnaireService.removeById(vo.getHealthQuestionnaireId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
