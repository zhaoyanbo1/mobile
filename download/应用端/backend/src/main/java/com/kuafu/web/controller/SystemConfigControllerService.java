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
import com.kuafu.web.entity.SystemConfig;
import com.kuafu.web.service.ISystemConfigService;
import com.kuafu.web.vo.SystemConfigPageVO;
import com.kuafu.web.vo.SystemConfigVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 系统配置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("SystemConfigControllerService")
@RequiredArgsConstructor
public class SystemConfigControllerService  {

    private final ISystemConfigService systemConfigService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( SystemConfigPageVO pageVO){
        IPage<SystemConfig> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<SystemConfig> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<SystemConfig> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(SystemConfig::getId);

            if(pageVO.getId() != null){
                queryWrapper.eq(SystemConfig::getId, pageVO.getId());
            }


        if(StringUtils.isNotEmpty(pageVO.getName())) {
//                        queryWrapper.like(SystemConfig::getName, pageVO.getName());
                        String s_string = pageVO.getName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(SystemConfig::getName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getChineseName())) {
//                        queryWrapper.like(SystemConfig::getChineseName, pageVO.getChineseName());
                        String s_string = pageVO.getChineseName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(SystemConfig::getChineseName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getDescription())) {
                queryWrapper.eq(SystemConfig::getDescription, pageVO.getDescription());
            }


        if(StringUtils.isNotEmpty(pageVO.getContent())) {
                queryWrapper.eq(SystemConfig::getContent, pageVO.getContent());
            }


        if(StringUtils.isNotEmpty(pageVO.getRemark())) {
                queryWrapper.eq(SystemConfig::getRemark, pageVO.getRemark());
            }


        if(StringUtils.isNotEmpty(pageVO.getType())) {
                queryWrapper.eq(SystemConfig::getType, pageVO.getType());
            }

            return ResultUtils.success(systemConfigService.page(page, queryWrapper));
        }


        public BaseResponse list(  SystemConfigVO vo){
            LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getName())) {
                            queryWrapper.like(SystemConfig::getName, vo.getName());
                }
            if(StringUtils.isNotEmpty(vo.getChineseName())) {
                            queryWrapper.like(SystemConfig::getChineseName, vo.getChineseName());
                }
            if(StringUtils.isNotEmpty(vo.getDescription())) {
                    queryWrapper.eq(SystemConfig::getDescription, vo.getDescription());
                }
            if(StringUtils.isNotEmpty(vo.getContent())) {
                    queryWrapper.eq(SystemConfig::getContent, vo.getContent());
                }
            if(StringUtils.isNotEmpty(vo.getRemark())) {
                    queryWrapper.eq(SystemConfig::getRemark, vo.getRemark());
                }
            if(StringUtils.isNotEmpty(vo.getType())) {
                    queryWrapper.eq(SystemConfig::getType, vo.getType());
                }
                return ResultUtils.success(systemConfigService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  SystemConfigVO vo) {
                SystemConfig entity =  SystemConfig.builder()
                    .name(vo.getName())
                    .chineseName(vo.getChineseName())
                    .description(vo.getDescription())
                    .content(vo.getContent())
                    .remark(vo.getRemark())
                    .type(vo.getType())
                .build();








                boolean flag =this.systemConfigService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("system_config").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( SystemConfigVO vo) {
                SystemConfig entity =  SystemConfig.builder()
                    .id(vo.getId())
                    .name(vo.getName())
                    .chineseName(vo.getChineseName())
                    .description(vo.getDescription())
                    .content(vo.getContent())
                    .remark(vo.getRemark())
                    .type(vo.getType())
                .build();



                        boolean flag = this.systemConfigService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( SystemConfigVO vo ) {
                SystemConfig entity = this.systemConfigService.getById(vo.getId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( SystemConfigVO vo) {
                boolean flag = this.systemConfigService.removeById(vo.getId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
