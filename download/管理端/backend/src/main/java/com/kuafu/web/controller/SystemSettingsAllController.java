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
import com.kuafu.web.entity.SystemSettingsAll;
import com.kuafu.web.service.ISystemSettingsAllService;
import com.kuafu.web.vo.SystemSettingsAllPageVO;

import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;


/**
 * <p> 系统设置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/systemSettingsAll")
@Api(tags = {"系统设置"})
public class SystemSettingsAllController  {

    private final ISystemSettingsAllService systemSettingsAllService;

    @PostMapping("page")
    @ApiOperation("全字段分页")
    public BaseResponse page(@RequestBody SystemSettingsAllPageVO pageVO){
        IPage<SystemSettingsAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
            queryWrapper.eq(SystemSettingsAll::getPassword, pageVO.getPassword());
        }
            if(pageVO.getReminderVolume() != null){
            queryWrapper.eq(SystemSettingsAll::getReminderVolume, pageVO.getReminderVolume());
        }
            if(pageVO.getLastLoginDate() != null){
            queryWrapper.eq(SystemSettingsAll::getLastLoginDate, pageVO.getLastLoginDate());
        }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(SystemSettingsAll::getPhoneNumber, pageVO.getPhoneNumber());
        }
            if(pageVO.getFontSize() != null){
            queryWrapper.eq(SystemSettingsAll::getFontSize, pageVO.getFontSize());
        }
            if(pageVO.getRegistrationDate() != null){
            queryWrapper.eq(SystemSettingsAll::getRegistrationDate, pageVO.getRegistrationDate());
        }
            if(pageVO.getQuestionnaireExported() != null){
            queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, pageVO.getQuestionnaireExported());
        }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {
            queryWrapper.like(SystemSettingsAll::getUsername, pageVO.getUsername());
        }
        return ResultUtils.success(systemSettingsAllService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("全字段列表")
    public BaseResponse list(@RequestBody SystemSettingsAllPageVO vo){
        LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
            queryWrapper.eq(SystemSettingsAll::getPassword, vo.getPassword());
        }
                if(vo.getReminderVolume() != null){
            queryWrapper.eq(SystemSettingsAll::getReminderVolume, vo.getReminderVolume());
        }
                if(vo.getLastLoginDate() != null){
            queryWrapper.eq(SystemSettingsAll::getLastLoginDate, vo.getLastLoginDate());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(SystemSettingsAll::getPhoneNumber, vo.getPhoneNumber());
        }
                if(vo.getFontSize() != null){
            queryWrapper.eq(SystemSettingsAll::getFontSize, vo.getFontSize());
        }
                if(vo.getRegistrationDate() != null){
            queryWrapper.eq(SystemSettingsAll::getRegistrationDate, vo.getRegistrationDate());
        }
                if(vo.getQuestionnaireExported() != null){
            queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, vo.getQuestionnaireExported());
        }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
            queryWrapper.like(SystemSettingsAll::getUsername, vo.getUsername());
        }
        return ResultUtils.success(systemSettingsAllService.list(queryWrapper));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer systemSettingsId) {
        SystemSettingsAll entity = this.systemSettingsAllService.getById(systemSettingsId);
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
        public void export(HttpServletResponse response, @RequestBody SystemSettingsAllPageVO vo) {

            LambdaQueryWrapper<SystemSettingsAll> queryWrapper = new LambdaQueryWrapper<>();

                    if(vo.getUserInfoUserInfoId1() != null){
                        queryWrapper.eq(SystemSettingsAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                    }
                if(StringUtils.isNotEmpty(vo.getPassword())) {
                        queryWrapper.eq(SystemSettingsAll::getPassword, vo.getPassword());
                    }
                    if(vo.getReminderVolume() != null){
                        queryWrapper.eq(SystemSettingsAll::getReminderVolume, vo.getReminderVolume());
                    }
                    if(vo.getLastLoginDate() != null){
                        queryWrapper.eq(SystemSettingsAll::getLastLoginDate, vo.getLastLoginDate());
                    }
                if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                        queryWrapper.eq(SystemSettingsAll::getPhoneNumber, vo.getPhoneNumber());
                    }
                    if(vo.getFontSize() != null){
                        queryWrapper.eq(SystemSettingsAll::getFontSize, vo.getFontSize());
                    }
                    if(vo.getRegistrationDate() != null){
                        queryWrapper.eq(SystemSettingsAll::getRegistrationDate, vo.getRegistrationDate());
                    }
                    if(vo.getQuestionnaireExported() != null){
                        queryWrapper.eq(SystemSettingsAll::getQuestionnaireExported, vo.getQuestionnaireExported());
                    }
                if(StringUtils.isNotEmpty(vo.getUsername())) {
                                queryWrapper.like(SystemSettingsAll::getUsername, vo.getUsername());
                    }
            List<SystemSettingsAll> list = systemSettingsAllService.list(queryWrapper);
            ExcelUtil<SystemSettingsAll> util = new ExcelUtil<>(SystemSettingsAll.class);
            util.exportExcel(response, list, "数据");
        }


}
