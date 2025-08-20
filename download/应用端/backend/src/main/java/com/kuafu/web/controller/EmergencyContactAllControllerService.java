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
import com.kuafu.web.entity.EmergencyContactAll;
import com.kuafu.web.service.IEmergencyContactAllService;
import com.kuafu.web.vo.EmergencyContactAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;
import org.springframework.context.annotation.Lazy;





/**
 * <p> 紧急联系人 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("EmergencyContactAllControllerService")
public class EmergencyContactAllControllerService implements IControllerService<EmergencyContactAll> {


@Autowired
    private  IEmergencyContactAllService emergencyContactAllService;








    public BaseResponse page( EmergencyContactAllPageVO pageVO){
        IPage<EmergencyContactAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<EmergencyContactAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ec.emergency_contact_id");
        LambdaQueryWrapper<EmergencyContactAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(EmergencyContactAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(EmergencyContactAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getEmergencyContactId() != null){
                queryWrapper.eq(EmergencyContactAll::getEmergencyContactId, pageVO.getEmergencyContactId());
            }
        if(StringUtils.isNotEmpty(pageVO.getName())) {

                        String s_string = pageVO.getName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(EmergencyContactAll::getName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(EmergencyContactAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
            if(pageVO.getLastLoginDate() != null){
                queryWrapper.eq(EmergencyContactAll::getLastLoginDate, pageVO.getLastLoginDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(EmergencyContactAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    emergencyContactAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });

        page.getRecords().forEach(item -> {
        });


            return ResultUtils.success(page);
        }

        public BaseResponse list(EmergencyContactAllPageVO vo){
            LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(EmergencyContactAll::getPassword, vo.getPassword());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(EmergencyContactAll::getRegistrationDate, vo.getRegistrationDate());
                }
            if(StringUtils.isNotEmpty(vo.getName())) {
                            queryWrapper.like(EmergencyContactAll::getName, vo.getName());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(EmergencyContactAll::getPhoneNumber, vo.getPhoneNumber());
                }
                if(vo.getLastLoginDate() != null){
                    queryWrapper.eq(EmergencyContactAll::getLastLoginDate, vo.getLastLoginDate());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(EmergencyContactAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(emergencyContactAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(EmergencyContactAllPageVO vo) {


                LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(EmergencyContactAll::getPassword, vo.getPassword());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(EmergencyContactAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getEmergencyContactId() != null){
                                    queryWrapper.eq(EmergencyContactAll::getEmergencyContactId, vo.getEmergencyContactId());
                                }


                            if(StringUtils.isNotEmpty(vo.getName())) {
                                            queryWrapper.like(EmergencyContactAll::getName, vo.getName());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(EmergencyContactAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                                if(vo.getLastLoginDate() != null){
                                    queryWrapper.eq(EmergencyContactAll::getLastLoginDate, vo.getLastLoginDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(EmergencyContactAll::getUsername, vo.getUsername());
                                }

                // EmergencyContactAll entity = this.emergencyContactAllService.getById(emergencyContactId);
                    final Page<EmergencyContactAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    emergencyContactAllService.pageNew(page, vo, queryWrapper);
                    final List<EmergencyContactAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public EmergencyContactAll getById(Serializable emergencyContactId) {

                    LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(EmergencyContactAll::getEmergencyContactId, emergencyContactId);

                    final Page<EmergencyContactAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    emergencyContactAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<EmergencyContactAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }
