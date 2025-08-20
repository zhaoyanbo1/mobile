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
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.vo.UserInfoPageVO;
import com.kuafu.web.vo.UserInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 用户信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("UserInfoControllerService")
@RequiredArgsConstructor
public class UserInfoControllerService  {

    private final IUserInfoService userInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( UserInfoPageVO pageVO){
        IPage<UserInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<UserInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<UserInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(UserInfo::getUserInfoId);

            if(pageVO.getUserInfoId() != null){
                queryWrapper.eq(UserInfo::getUserInfoId, pageVO.getUserInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.eq(UserInfo::getPhoneNumber, pageVO.getPhoneNumber());
            }


        if(StringUtils.isNotEmpty(pageVO.getUsername())) {
//                        queryWrapper.like(UserInfo::getUsername, pageVO.getUsername());
                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(UserInfo::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.eq(UserInfo::getPassword, pageVO.getPassword());
            }


            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(UserInfo::getRegistrationDate, pageVO.getRegistrationDate());
            }


            if(pageVO.getLastLoginDate() != null){
                queryWrapper.eq(UserInfo::getLastLoginDate, pageVO.getLastLoginDate());
            }

            return ResultUtils.success(userInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  UserInfoVO vo){
            LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(UserInfo::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(UserInfo::getUsername, vo.getUsername());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(UserInfo::getPassword, vo.getPassword());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(UserInfo::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getLastLoginDate() != null){
                    queryWrapper.eq(UserInfo::getLastLoginDate, vo.getLastLoginDate());
                }
                return ResultUtils.success(userInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  UserInfoVO vo) {
                UserInfo entity =  UserInfo.builder()
                    .phoneNumber(vo.getPhoneNumber())
                    .username(vo.getUsername())
                    .password(vo.getPassword())
                    .registrationDate(vo.getRegistrationDate())
                    .lastLoginDate(vo.getLastLoginDate())
                .build();








                boolean flag =this.userInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("user_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getUserInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( UserInfoVO vo) {
                UserInfo entity =  UserInfo.builder()
                    .userInfoId(vo.getUserInfoId())
                    .phoneNumber(vo.getPhoneNumber())
                    .username(vo.getUsername())
                    .password(vo.getPassword())
                    .registrationDate(vo.getRegistrationDate())
                    .lastLoginDate(vo.getLastLoginDate())
                .build();



                        boolean flag = this.userInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getUserInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( UserInfoVO vo ) {
                UserInfo entity = this.userInfoService.getById(vo.getUserInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( UserInfoVO vo) {
                boolean flag = this.userInfoService.removeById(vo.getUserInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
