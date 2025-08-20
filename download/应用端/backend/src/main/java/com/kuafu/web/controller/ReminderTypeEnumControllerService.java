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
import com.kuafu.web.entity.ReminderTypeEnum;
import com.kuafu.web.service.IReminderTypeEnumService;
import com.kuafu.web.vo.ReminderTypeEnumPageVO;
import com.kuafu.web.vo.ReminderTypeEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 提醒类型枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("ReminderTypeEnumControllerService")
@RequiredArgsConstructor
public class ReminderTypeEnumControllerService  {

    private final IReminderTypeEnumService reminderTypeEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ReminderTypeEnumPageVO pageVO){
        IPage<ReminderTypeEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ReminderTypeEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ReminderTypeEnum::getReminderTypeEnumId);

            if(pageVO.getReminderTypeEnumId() != null){
                queryWrapper.eq(ReminderTypeEnum::getReminderTypeEnumId, pageVO.getReminderTypeEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTypeName())) {
//                        queryWrapper.like(ReminderTypeEnum::getTypeName, pageVO.getTypeName());
                        String s_string = pageVO.getTypeName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ReminderTypeEnum::getTypeName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(reminderTypeEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  ReminderTypeEnumVO vo){
            LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTypeName())) {
                            queryWrapper.like(ReminderTypeEnum::getTypeName, vo.getTypeName());
                }
                return ResultUtils.success(reminderTypeEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ReminderTypeEnumVO vo) {
                ReminderTypeEnum entity =  ReminderTypeEnum.builder()
                    .typeName(vo.getTypeName())
                .build();








                boolean flag =this.reminderTypeEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("reminder_type_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getReminderTypeEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ReminderTypeEnumVO vo) {
                ReminderTypeEnum entity =  ReminderTypeEnum.builder()
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .typeName(vo.getTypeName())
                .build();



                        boolean flag = this.reminderTypeEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getReminderTypeEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ReminderTypeEnumVO vo ) {
                ReminderTypeEnum entity = this.reminderTypeEnumService.getById(vo.getReminderTypeEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ReminderTypeEnumVO vo) {
                boolean flag = this.reminderTypeEnumService.removeById(vo.getReminderTypeEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
