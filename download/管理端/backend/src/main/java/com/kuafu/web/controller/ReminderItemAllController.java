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
import com.kuafu.web.entity.ReminderItemAll;
import com.kuafu.web.service.IReminderItemAllService;
import com.kuafu.web.vo.ReminderItemAllPageVO;

import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;


/**
 * <p> 提醒事项 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminderItemAll")
@Api(tags = {"提醒事项"})
public class ReminderItemAllController  {

    private final IReminderItemAllService reminderItemAllService;

    @PostMapping("page")
    @ApiOperation("全字段分页")
    public BaseResponse page(@RequestBody ReminderItemAllPageVO pageVO){
        IPage<ReminderItemAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
        if(StringUtils.isNotEmpty(pageVO.getMedicinePhotoResourceKey())) {
            queryWrapper.eq(ReminderItemAll::getMedicinePhotoResourceKey, pageVO.getMedicinePhotoResourceKey());
        }
        if(StringUtils.isNotEmpty(pageVO.getLocationAddress())) {
            queryWrapper.eq(ReminderItemAll::getLocationAddress, pageVO.getLocationAddress());
        }
        if(StringUtils.isNotEmpty(pageVO.getMedicineDosage())) {
            queryWrapper.eq(ReminderItemAll::getMedicineDosage, pageVO.getMedicineDosage());
        }
            if(pageVO.getLastLoginDate() != null){
            queryWrapper.eq(ReminderItemAll::getLastLoginDate, pageVO.getLastLoginDate());
        }
            if(pageVO.getDietRecipeId() != null){
            queryWrapper.eq(ReminderItemAll::getDietRecipeId, pageVO.getDietRecipeId());
        }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(ReminderItemAll::getPhoneNumber, pageVO.getPhoneNumber());
        }
        if(StringUtils.isNotEmpty(pageVO.getDescription())) {
            queryWrapper.eq(ReminderItemAll::getDescription, pageVO.getDescription());
        }
        if(StringUtils.isNotEmpty(pageVO.getLocationLongitude())) {
            queryWrapper.eq(ReminderItemAll::getLocationLongitude, pageVO.getLocationLongitude());
        }
            if(pageVO.getRegistrationDate() != null){
            queryWrapper.eq(ReminderItemAll::getRegistrationDate, pageVO.getRegistrationDate());
        }
        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
            queryWrapper.eq(ReminderItemAll::getTitle, pageVO.getTitle());
        }
            if(pageVO.getReminderTime() != null){
            queryWrapper.eq(ReminderItemAll::getReminderTime, pageVO.getReminderTime());
        }
            if(pageVO.getIsCompleted() != null){
            queryWrapper.eq(ReminderItemAll::getIsCompleted, pageVO.getIsCompleted());
        }
        if(StringUtils.isNotEmpty(pageVO.getLocationLatitude())) {
            queryWrapper.eq(ReminderItemAll::getLocationLatitude, pageVO.getLocationLatitude());
        }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
            queryWrapper.eq(ReminderItemAll::getPassword, pageVO.getPassword());
        }
            if(pageVO.getCreationTime() != null){
            queryWrapper.eq(ReminderItemAll::getCreationTime, pageVO.getCreationTime());
        }
            if(pageVO.getUpdateTime() != null){
            queryWrapper.eq(ReminderItemAll::getUpdateTime, pageVO.getUpdateTime());
        }
            if(pageVO.getReminderTypeEnumId() != null){
            queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, pageVO.getReminderTypeEnumId());
        }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {
            queryWrapper.like(ReminderItemAll::getUsername, pageVO.getUsername());
        }
        return ResultUtils.success(reminderItemAllService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("全字段列表")
    public BaseResponse list(@RequestBody ReminderItemAllPageVO vo){
        LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getMedicinePhotoResourceKey())) {
            queryWrapper.eq(ReminderItemAll::getMedicinePhotoResourceKey, vo.getMedicinePhotoResourceKey());
        }
            if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
            queryWrapper.eq(ReminderItemAll::getLocationAddress, vo.getLocationAddress());
        }
            if(StringUtils.isNotEmpty(vo.getMedicineDosage())) {
            queryWrapper.eq(ReminderItemAll::getMedicineDosage, vo.getMedicineDosage());
        }
                if(vo.getLastLoginDate() != null){
            queryWrapper.eq(ReminderItemAll::getLastLoginDate, vo.getLastLoginDate());
        }
                if(vo.getDietRecipeId() != null){
            queryWrapper.eq(ReminderItemAll::getDietRecipeId, vo.getDietRecipeId());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(ReminderItemAll::getPhoneNumber, vo.getPhoneNumber());
        }
            if(StringUtils.isNotEmpty(vo.getDescription())) {
            queryWrapper.eq(ReminderItemAll::getDescription, vo.getDescription());
        }
            if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
            queryWrapper.eq(ReminderItemAll::getLocationLongitude, vo.getLocationLongitude());
        }
                if(vo.getRegistrationDate() != null){
            queryWrapper.eq(ReminderItemAll::getRegistrationDate, vo.getRegistrationDate());
        }
            if(StringUtils.isNotEmpty(vo.getTitle())) {
            queryWrapper.eq(ReminderItemAll::getTitle, vo.getTitle());
        }
                if(vo.getReminderTime() != null){
            queryWrapper.eq(ReminderItemAll::getReminderTime, vo.getReminderTime());
        }
                if(vo.getIsCompleted() != null){
            queryWrapper.eq(ReminderItemAll::getIsCompleted, vo.getIsCompleted());
        }
            if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
            queryWrapper.eq(ReminderItemAll::getLocationLatitude, vo.getLocationLatitude());
        }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
            queryWrapper.eq(ReminderItemAll::getPassword, vo.getPassword());
        }
                if(vo.getCreationTime() != null){
            queryWrapper.eq(ReminderItemAll::getCreationTime, vo.getCreationTime());
        }
                if(vo.getUpdateTime() != null){
            queryWrapper.eq(ReminderItemAll::getUpdateTime, vo.getUpdateTime());
        }
                if(vo.getReminderTypeEnumId() != null){
            queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, vo.getReminderTypeEnumId());
        }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
            queryWrapper.like(ReminderItemAll::getUsername, vo.getUsername());
        }
        return ResultUtils.success(reminderItemAllService.list(queryWrapper));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer reminderItemId) {
        ReminderItemAll entity = this.reminderItemAllService.getById(reminderItemId);
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
        public void export(HttpServletResponse response, @RequestBody ReminderItemAllPageVO vo) {

            LambdaQueryWrapper<ReminderItemAll> queryWrapper = new LambdaQueryWrapper<>();

                    if(vo.getUserInfoUserInfoId1() != null){
                        queryWrapper.eq(ReminderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                    }
                if(StringUtils.isNotEmpty(vo.getMedicinePhotoResourceKey())) {
                        queryWrapper.eq(ReminderItemAll::getMedicinePhotoResourceKey, vo.getMedicinePhotoResourceKey());
                    }
                if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
                        queryWrapper.eq(ReminderItemAll::getLocationAddress, vo.getLocationAddress());
                    }
                if(StringUtils.isNotEmpty(vo.getMedicineDosage())) {
                        queryWrapper.eq(ReminderItemAll::getMedicineDosage, vo.getMedicineDosage());
                    }
                    if(vo.getLastLoginDate() != null){
                        queryWrapper.eq(ReminderItemAll::getLastLoginDate, vo.getLastLoginDate());
                    }
                    if(vo.getDietRecipeId() != null){
                        queryWrapper.eq(ReminderItemAll::getDietRecipeId, vo.getDietRecipeId());
                    }
                if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                        queryWrapper.eq(ReminderItemAll::getPhoneNumber, vo.getPhoneNumber());
                    }
                if(StringUtils.isNotEmpty(vo.getDescription())) {
                        queryWrapper.eq(ReminderItemAll::getDescription, vo.getDescription());
                    }
                if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
                        queryWrapper.eq(ReminderItemAll::getLocationLongitude, vo.getLocationLongitude());
                    }
                    if(vo.getRegistrationDate() != null){
                        queryWrapper.eq(ReminderItemAll::getRegistrationDate, vo.getRegistrationDate());
                    }
                if(StringUtils.isNotEmpty(vo.getTitle())) {
                        queryWrapper.eq(ReminderItemAll::getTitle, vo.getTitle());
                    }
                    if(vo.getReminderTime() != null){
                        queryWrapper.eq(ReminderItemAll::getReminderTime, vo.getReminderTime());
                    }
                    if(vo.getIsCompleted() != null){
                        queryWrapper.eq(ReminderItemAll::getIsCompleted, vo.getIsCompleted());
                    }
                if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
                        queryWrapper.eq(ReminderItemAll::getLocationLatitude, vo.getLocationLatitude());
                    }
                if(StringUtils.isNotEmpty(vo.getPassword())) {
                        queryWrapper.eq(ReminderItemAll::getPassword, vo.getPassword());
                    }
                    if(vo.getCreationTime() != null){
                        queryWrapper.eq(ReminderItemAll::getCreationTime, vo.getCreationTime());
                    }
                    if(vo.getUpdateTime() != null){
                        queryWrapper.eq(ReminderItemAll::getUpdateTime, vo.getUpdateTime());
                    }
                    if(vo.getReminderTypeEnumId() != null){
                        queryWrapper.eq(ReminderItemAll::getReminderTypeEnumId, vo.getReminderTypeEnumId());
                    }
                if(StringUtils.isNotEmpty(vo.getUsername())) {
                                queryWrapper.like(ReminderItemAll::getUsername, vo.getUsername());
                    }
            List<ReminderItemAll> list = reminderItemAllService.list(queryWrapper);
            ExcelUtil<ReminderItemAll> util = new ExcelUtil<>(ReminderItemAll.class);
            util.exportExcel(response, list, "数据");
        }


}
