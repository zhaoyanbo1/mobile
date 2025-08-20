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
import com.kuafu.web.entity.ReminderItemAll;
import com.kuafu.web.service.IReminderItemAllService;
import com.kuafu.web.vo.ReminderItemAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;
import org.springframework.context.annotation.Lazy;





/**
 * <p> 提醒事项 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("ReminderItemAllControllerService")
public class ReminderItemAllControllerService implements IControllerService<ReminderItemAll> {


@Autowired
    private  IReminderItemAllService reminderItemAllService;








    public BaseResponse page( ReminderItemAllPageVO pageVO){
        IPage<ReminderItemAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<ReminderItemAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ri.reminder_item_id");
        LambdaQueryWrapper<ReminderItemAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getReminderTime() != null){
                queryWrapper.eq(ReminderItemAll::getReminderTime, pageVO.getReminderTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getLocationAddress())) {
                queryWrapper.like(ReminderItemAll::getLocationAddress, pageVO.getLocationAddress());
            }
            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(ReminderItemAll::getCreationTime, pageVO.getCreationTime());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getLocationLatitude())) {
                queryWrapper.like(ReminderItemAll::getLocationLatitude, pageVO.getLocationLatitude());
            }
        if(StringUtils.isNotEmpty(pageVO.getDescription())) {
                queryWrapper.like(ReminderItemAll::getDescription, pageVO.getDescription());
            }
        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.like(ReminderItemAll::getTitle, pageVO.getTitle());
            }
            if(pageVO.getLastLoginDate() != null){
                queryWrapper.eq(ReminderItemAll::getLastLoginDate, pageVO.getLastLoginDate());
            }
            if(pageVO.getIsCompleted() != null){
                queryWrapper.eq(ReminderItemAll::getIsCompleted, pageVO.getIsCompleted());
            }
        if(StringUtils.isNotEmpty(pageVO.getMedicineDosage())) {
                queryWrapper.like(ReminderItemAll::getMedicineDosage, pageVO.getMedicineDosage());
            }
            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(ReminderItemAll::getUpdateTime, pageVO.getUpdateTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(ReminderItemAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(ReminderItemAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getReminderItemId() != null){
                queryWrapper.eq(ReminderItemAll::getReminderItemId, pageVO.getReminderItemId());
            }
            if(pageVO.getDietRecipeId() != null){
                queryWrapper.eq(ReminderItemAll::getDietRecipeId, pageVO.getDietRecipeId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(ReminderItemAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
            if(pageVO.getReminderTypeEnumId() != null){
                queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, pageVO.getReminderTypeEnumId());
            }
        if(StringUtils.isNotEmpty(pageVO.getLocationLongitude())) {
                queryWrapper.like(ReminderItemAll::getLocationLongitude, pageVO.getLocationLongitude());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ReminderItemAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    reminderItemAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });

        page.getRecords().forEach(item -> {
        });


            return ResultUtils.success(page);
        }

        public BaseResponse list(ReminderItemAllPageVO vo){
            LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getReminderTime() != null){
                    queryWrapper.eq(ReminderItemAll::getReminderTime, vo.getReminderTime());
                }
            if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
                    queryWrapper.eq(ReminderItemAll::getLocationAddress, vo.getLocationAddress());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(ReminderItemAll::getCreationTime, vo.getCreationTime());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
                    queryWrapper.eq(ReminderItemAll::getLocationLatitude, vo.getLocationLatitude());
                }
            if(StringUtils.isNotEmpty(vo.getDescription())) {
                    queryWrapper.eq(ReminderItemAll::getDescription, vo.getDescription());
                }
            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(ReminderItemAll::getTitle, vo.getTitle());
                }
                if(vo.getLastLoginDate() != null){
                    queryWrapper.eq(ReminderItemAll::getLastLoginDate, vo.getLastLoginDate());
                }
                if(vo.getIsCompleted() != null){
                    queryWrapper.eq(ReminderItemAll::getIsCompleted, vo.getIsCompleted());
                }
            if(StringUtils.isNotEmpty(vo.getMedicineDosage())) {
                    queryWrapper.eq(ReminderItemAll::getMedicineDosage, vo.getMedicineDosage());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(ReminderItemAll::getUpdateTime, vo.getUpdateTime());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(ReminderItemAll::getPassword, vo.getPassword());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(ReminderItemAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getDietRecipeId() != null){
                    queryWrapper.eq(ReminderItemAll::getDietRecipeId, vo.getDietRecipeId());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(ReminderItemAll::getPhoneNumber, vo.getPhoneNumber());
                }
                if(vo.getReminderTypeEnumId() != null){
                    queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, vo.getReminderTypeEnumId());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
                    queryWrapper.eq(ReminderItemAll::getLocationLongitude, vo.getLocationLongitude());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(ReminderItemAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(reminderItemAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(ReminderItemAllPageVO vo) {


                LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getReminderTime() != null){
                                    queryWrapper.eq(ReminderItemAll::getReminderTime, vo.getReminderTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
                                    queryWrapper.eq(ReminderItemAll::getLocationAddress, vo.getLocationAddress());
                                }


                                if(vo.getCreationTime() != null){
                                    queryWrapper.eq(ReminderItemAll::getCreationTime, vo.getCreationTime());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
                                    queryWrapper.eq(ReminderItemAll::getLocationLatitude, vo.getLocationLatitude());
                                }


                            if(StringUtils.isNotEmpty(vo.getDescription())) {
                                    queryWrapper.eq(ReminderItemAll::getDescription, vo.getDescription());
                                }


                            if(StringUtils.isNotEmpty(vo.getTitle())) {
                                    queryWrapper.eq(ReminderItemAll::getTitle, vo.getTitle());
                                }


                                if(vo.getLastLoginDate() != null){
                                    queryWrapper.eq(ReminderItemAll::getLastLoginDate, vo.getLastLoginDate());
                                }


                                if(vo.getIsCompleted() != null){
                                    queryWrapper.eq(ReminderItemAll::getIsCompleted, vo.getIsCompleted());
                                }


                            if(StringUtils.isNotEmpty(vo.getMedicineDosage())) {
                                    queryWrapper.eq(ReminderItemAll::getMedicineDosage, vo.getMedicineDosage());
                                }


                                if(vo.getUpdateTime() != null){
                                    queryWrapper.eq(ReminderItemAll::getUpdateTime, vo.getUpdateTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(ReminderItemAll::getPassword, vo.getPassword());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(ReminderItemAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getReminderItemId() != null){
                                    queryWrapper.eq(ReminderItemAll::getReminderItemId, vo.getReminderItemId());
                                }


                                if(vo.getDietRecipeId() != null){
                                    queryWrapper.eq(ReminderItemAll::getDietRecipeId, vo.getDietRecipeId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(ReminderItemAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                                if(vo.getReminderTypeEnumId() != null){
                                    queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, vo.getReminderTypeEnumId());
                                }


                            if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
                                    queryWrapper.eq(ReminderItemAll::getLocationLongitude, vo.getLocationLongitude());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(ReminderItemAll::getUsername, vo.getUsername());
                                }

                // ReminderItemAll entity = this.reminderItemAllService.getById(reminderItemId);
                    final Page<ReminderItemAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    reminderItemAllService.pageNew(page, vo, queryWrapper);
                    final List<ReminderItemAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public ReminderItemAll getById(Serializable reminderItemId) {

                    LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(ReminderItemAll::getReminderItemId, reminderItemId);

                    final Page<ReminderItemAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    reminderItemAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<ReminderItemAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }
