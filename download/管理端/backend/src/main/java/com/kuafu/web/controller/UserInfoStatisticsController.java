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
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoStatisticsService;
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
@RequestMapping("/userInfoStatistics")

public class UserInfoStatisticsController  {

    private final IUserInfoStatisticsService userInfoStatisticsService;


    @PostMapping("user_info_registration_date_datetime_statistic_e1bf3d5d_count")
    public BaseResponse user_info_registration_date_datetime_statistic_e1bf3d5d_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_e1bf3d5d_count(queryWrapper));
    }
    @PostMapping("user_info_registration_date_datetime_statistic_683a2401_count")
    public BaseResponse user_info_registration_date_datetime_statistic_683a2401_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_683a2401_count(queryWrapper));
    }
    @PostMapping("user_info_registration_date_datetime_statistic_1e61d94d_count")
    public BaseResponse user_info_registration_date_datetime_statistic_1e61d94d_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_1e61d94d_count(queryWrapper));
    }
    @PostMapping("user_info_last_login_date_datetime_statistic_10ddf510_count")
    public BaseResponse user_info_last_login_date_datetime_statistic_10ddf510_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_last_login_date_datetime_statistic_10ddf510_count(queryWrapper));
    }
    @PostMapping("user_info_last_login_date_datetime_statistic_65a6ff33_count")
    public BaseResponse user_info_last_login_date_datetime_statistic_65a6ff33_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_last_login_date_datetime_statistic_65a6ff33_count(queryWrapper));
    }
    @PostMapping("user_info_last_login_date_datetime_statistic_d6db3a40_count")
    public BaseResponse user_info_last_login_date_datetime_statistic_d6db3a40_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_last_login_date_datetime_statistic_d6db3a40_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<UserInfo> queryWrapper, UserInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getPhoneNumber())) {
                queryWrapper.eq(UserInfo::getPhoneNumber, statisticVo.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(statisticVo.getUsername())) {
                        queryWrapper.like(UserInfo::getUsername, statisticVo.getUsername());
            }
        if(StringUtils.isNotEmpty(statisticVo.getPassword())) {
                queryWrapper.eq(UserInfo::getPassword, statisticVo.getPassword());
            }
            if(statisticVo.getRegistrationDate() != null){
                queryWrapper.eq(UserInfo::getRegistrationDate, statisticVo.getRegistrationDate());
            }
            if(statisticVo.getLastLoginDate() != null){
                queryWrapper.eq(UserInfo::getLastLoginDate, statisticVo.getLastLoginDate());
            }
    }



}
