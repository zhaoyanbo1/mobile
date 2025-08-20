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
import com.kuafu.web.entity.SystemSettingsAll;
import com.kuafu.web.service.ISystemSettingsAllService;
import com.kuafu.web.vo.SystemSettingsAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;
import org.springframework.context.annotation.Lazy;





/**
 * <p> 系统设置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("SystemSettingsAllControllerService")
public class SystemSettingsAllControllerService implements IControllerService<SystemSettingsAll> {


@Autowired
    private  ISystemSettingsAllService systemSettingsAllService;








    public BaseResponse page( SystemSettingsAllPageVO pageVO){
        IPage<SystemSettingsAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<SystemSettingsAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ss.system_settings_id");
        LambdaQueryWrapper<SystemSettingsAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(SystemSettingsAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(SystemSettingsAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getReminderVolume() != null){
                queryWrapper.eq(SystemSettingsAll::getReminderVolume, pageVO.getReminderVolume());
            }
            if(pageVO.getSystemSettingsId() != null){
                queryWrapper.eq(SystemSettingsAll::getSystemSettingsId, pageVO.getSystemSettingsId());
            }
            if(pageVO.getFontSize() != null){
                queryWrapper.eq(SystemSettingsAll::getFontSize, pageVO.getFontSize());
            }
            if(pageVO.getQuestionnaireExported() != null){
                queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, pageVO.getQuestionnaireExported());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(SystemSettingsAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
            if(pageVO.getLastLoginDate() != null){
                queryWrapper.eq(SystemSettingsAll::getLastLoginDate, pageVO.getLastLoginDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(SystemSettingsAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    systemSettingsAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });

        page.getRecords().forEach(item -> {
        });


            return ResultUtils.success(page);
        }

        public BaseResponse list(SystemSettingsAllPageVO vo){
            LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(SystemSettingsAll::getPassword, vo.getPassword());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(SystemSettingsAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getReminderVolume() != null){
                    queryWrapper.eq(SystemSettingsAll::getReminderVolume, vo.getReminderVolume());
                }
                if(vo.getFontSize() != null){
                    queryWrapper.eq(SystemSettingsAll::getFontSize, vo.getFontSize());
                }
                if(vo.getQuestionnaireExported() != null){
                    queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, vo.getQuestionnaireExported());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(SystemSettingsAll::getPhoneNumber, vo.getPhoneNumber());
                }
                if(vo.getLastLoginDate() != null){
                    queryWrapper.eq(SystemSettingsAll::getLastLoginDate, vo.getLastLoginDate());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(SystemSettingsAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(systemSettingsAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(SystemSettingsAllPageVO vo) {


                LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(SystemSettingsAll::getPassword, vo.getPassword());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(SystemSettingsAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getReminderVolume() != null){
                                    queryWrapper.eq(SystemSettingsAll::getReminderVolume, vo.getReminderVolume());
                                }


                                if(vo.getSystemSettingsId() != null){
                                    queryWrapper.eq(SystemSettingsAll::getSystemSettingsId, vo.getSystemSettingsId());
                                }


                                if(vo.getFontSize() != null){
                                    queryWrapper.eq(SystemSettingsAll::getFontSize, vo.getFontSize());
                                }


                                if(vo.getQuestionnaireExported() != null){
                                    queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, vo.getQuestionnaireExported());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(SystemSettingsAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                                if(vo.getLastLoginDate() != null){
                                    queryWrapper.eq(SystemSettingsAll::getLastLoginDate, vo.getLastLoginDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(SystemSettingsAll::getUsername, vo.getUsername());
                                }

                // SystemSettingsAll entity = this.systemSettingsAllService.getById(systemSettingsId);
                    final Page<SystemSettingsAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    systemSettingsAllService.pageNew(page, vo, queryWrapper);
                    final List<SystemSettingsAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public SystemSettingsAll getById(Serializable systemSettingsId) {

                    LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(SystemSettingsAll::getSystemSettingsId, systemSettingsId);

                    final Page<SystemSettingsAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    systemSettingsAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<SystemSettingsAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }
