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
import com.kuafu.web.entity.EmergencyContact;
import com.kuafu.web.service.IEmergencyContactService;
import com.kuafu.web.vo.EmergencyContactPageVO;
import com.kuafu.web.vo.EmergencyContactVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 紧急联系人 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("EmergencyContactControllerService")
@RequiredArgsConstructor
public class EmergencyContactControllerService  {

    private final IEmergencyContactService emergencyContactService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( EmergencyContactPageVO pageVO){
        IPage<EmergencyContact> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<EmergencyContact> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<EmergencyContact> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(EmergencyContact::getEmergencyContactId);

            if(pageVO.getEmergencyContactId() != null){
                queryWrapper.eq(EmergencyContact::getEmergencyContactId, pageVO.getEmergencyContactId());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getName())) {
//                        queryWrapper.like(EmergencyContact::getName, pageVO.getName());
                        String s_string = pageVO.getName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(EmergencyContact::getName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.eq(EmergencyContact::getPhoneNumber, pageVO.getPhoneNumber());
            }

            return ResultUtils.success(emergencyContactService.page(page, queryWrapper));
        }


        public BaseResponse list(  EmergencyContactVO vo){
            LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getName())) {
                            queryWrapper.like(EmergencyContact::getName, vo.getName());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(EmergencyContact::getPhoneNumber, vo.getPhoneNumber());
                }
                return ResultUtils.success(emergencyContactService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  EmergencyContactVO vo) {
                EmergencyContact entity =  EmergencyContact.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .phoneNumber(vo.getPhoneNumber())
                .build();








                boolean flag =this.emergencyContactService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("emergency_contact").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getEmergencyContactId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( EmergencyContactVO vo) {
                EmergencyContact entity =  EmergencyContact.builder()
                    .emergencyContactId(vo.getEmergencyContactId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .phoneNumber(vo.getPhoneNumber())
                .build();



                        boolean flag = this.emergencyContactService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getEmergencyContactId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( EmergencyContactVO vo ) {
                EmergencyContact entity = this.emergencyContactService.getById(vo.getEmergencyContactId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( EmergencyContactVO vo) {
                boolean flag = this.emergencyContactService.removeById(vo.getEmergencyContactId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
