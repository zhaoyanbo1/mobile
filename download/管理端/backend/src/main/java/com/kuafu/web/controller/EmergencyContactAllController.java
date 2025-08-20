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
import com.kuafu.web.entity.EmergencyContactAll;
import com.kuafu.web.service.IEmergencyContactAllService;
import com.kuafu.web.vo.EmergencyContactAllPageVO;

import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;


/**
 * <p> 紧急联系人 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/emergencyContactAll")
@Api(tags = {"紧急联系人"})
public class EmergencyContactAllController  {

    private final IEmergencyContactAllService emergencyContactAllService;

    @PostMapping("page")
    @ApiOperation("全字段分页")
    public BaseResponse page(@RequestBody EmergencyContactAllPageVO pageVO){
        IPage<EmergencyContactAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
            queryWrapper.eq(EmergencyContactAll::getPassword, pageVO.getPassword());
        }
            if(pageVO.getLastLoginDate() != null){
            queryWrapper.eq(EmergencyContactAll::getLastLoginDate, pageVO.getLastLoginDate());
        }
        if(StringUtils.isNotEmpty(pageVO.getName())) {
            queryWrapper.like(EmergencyContactAll::getName, pageVO.getName());
        }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(EmergencyContactAll::getPhoneNumber, pageVO.getPhoneNumber());
        }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumberUi())) {
            queryWrapper.eq(EmergencyContactAll::getPhoneNumberUi, pageVO.getPhoneNumberUi());
        }
            if(pageVO.getRegistrationDate() != null){
            queryWrapper.eq(EmergencyContactAll::getRegistrationDate, pageVO.getRegistrationDate());
        }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {
            queryWrapper.like(EmergencyContactAll::getUsername, pageVO.getUsername());
        }
        return ResultUtils.success(emergencyContactAllService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("全字段列表")
    public BaseResponse list(@RequestBody EmergencyContactAllPageVO vo){
        LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
            queryWrapper.eq(EmergencyContactAll::getPassword, vo.getPassword());
        }
                if(vo.getLastLoginDate() != null){
            queryWrapper.eq(EmergencyContactAll::getLastLoginDate, vo.getLastLoginDate());
        }
            if(StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like(EmergencyContactAll::getName, vo.getName());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(EmergencyContactAll::getPhoneNumber, vo.getPhoneNumber());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumberUi())) {
            queryWrapper.eq(EmergencyContactAll::getPhoneNumberUi, vo.getPhoneNumberUi());
        }
                if(vo.getRegistrationDate() != null){
            queryWrapper.eq(EmergencyContactAll::getRegistrationDate, vo.getRegistrationDate());
        }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
            queryWrapper.like(EmergencyContactAll::getUsername, vo.getUsername());
        }
        return ResultUtils.success(emergencyContactAllService.list(queryWrapper));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer emergencyContactId) {
        EmergencyContactAll entity = this.emergencyContactAllService.getById(emergencyContactId);
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
        public void export(HttpServletResponse response, @RequestBody EmergencyContactAllPageVO vo) {

            LambdaQueryWrapper<EmergencyContactAll> queryWrapper = new LambdaQueryWrapper<>();

                    if(vo.getUserInfoUserInfoId1() != null){
                        queryWrapper.eq(EmergencyContactAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                    }
                if(StringUtils.isNotEmpty(vo.getPassword())) {
                        queryWrapper.eq(EmergencyContactAll::getPassword, vo.getPassword());
                    }
                    if(vo.getLastLoginDate() != null){
                        queryWrapper.eq(EmergencyContactAll::getLastLoginDate, vo.getLastLoginDate());
                    }
                if(StringUtils.isNotEmpty(vo.getName())) {
                                queryWrapper.like(EmergencyContactAll::getName, vo.getName());
                    }
                if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                        queryWrapper.eq(EmergencyContactAll::getPhoneNumber, vo.getPhoneNumber());
                    }
                if(StringUtils.isNotEmpty(vo.getPhoneNumberUi())) {
                        queryWrapper.eq(EmergencyContactAll::getPhoneNumberUi, vo.getPhoneNumberUi());
                    }
                    if(vo.getRegistrationDate() != null){
                        queryWrapper.eq(EmergencyContactAll::getRegistrationDate, vo.getRegistrationDate());
                    }
                if(StringUtils.isNotEmpty(vo.getUsername())) {
                                queryWrapper.like(EmergencyContactAll::getUsername, vo.getUsername());
                    }
            List<EmergencyContactAll> list = emergencyContactAllService.list(queryWrapper);
            ExcelUtil<EmergencyContactAll> util = new ExcelUtil<>(EmergencyContactAll.class);
            util.exportExcel(response, list, "数据");
        }


}
