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
import com.kuafu.web.entity.SystemSettings;
import com.kuafu.web.service.ISystemSettingsService;
import com.kuafu.web.vo.SystemSettingsPageVO;
import com.kuafu.web.vo.SystemSettingsVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 系统设置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("SystemSettingsControllerService")
@RequiredArgsConstructor
public class SystemSettingsControllerService  {

    private final ISystemSettingsService systemSettingsService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( SystemSettingsPageVO pageVO){
        IPage<SystemSettings> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<SystemSettings> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<SystemSettings> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(SystemSettings::getSystemSettingsId);

            if(pageVO.getSystemSettingsId() != null){
                queryWrapper.eq(SystemSettings::getSystemSettingsId, pageVO.getSystemSettingsId());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(SystemSettings::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


            if(pageVO.getReminderVolume() != null){
                queryWrapper.eq(SystemSettings::getReminderVolume, pageVO.getReminderVolume());
            }


            if(pageVO.getFontSize() != null){
                queryWrapper.eq(SystemSettings::getFontSize, pageVO.getFontSize());
            }


            if(pageVO.getQuestionnaireExported() != null){
                queryWrapper.eq(SystemSettings::getQuestionnaireExported, pageVO.getQuestionnaireExported());
            }

            return ResultUtils.success(systemSettingsService.page(page, queryWrapper));
        }


        public BaseResponse list(  SystemSettingsVO vo){
            LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(SystemSettings::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getReminderVolume() != null){
                    queryWrapper.eq(SystemSettings::getReminderVolume, vo.getReminderVolume());
                }
                if(vo.getFontSize() != null){
                    queryWrapper.eq(SystemSettings::getFontSize, vo.getFontSize());
                }
                if(vo.getQuestionnaireExported() != null){
                    queryWrapper.eq(SystemSettings::getQuestionnaireExported, vo.getQuestionnaireExported());
                }
                return ResultUtils.success(systemSettingsService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  SystemSettingsVO vo) {
                SystemSettings entity =  SystemSettings.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderVolume(vo.getReminderVolume())
                    .fontSize(vo.getFontSize())
                    .questionnaireExported(vo.getQuestionnaireExported())
                .build();








                boolean flag =this.systemSettingsService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("system_settings").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getSystemSettingsId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( SystemSettingsVO vo) {
                SystemSettings entity =  SystemSettings.builder()
                    .systemSettingsId(vo.getSystemSettingsId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderVolume(vo.getReminderVolume())
                    .fontSize(vo.getFontSize())
                    .questionnaireExported(vo.getQuestionnaireExported())
                .build();



                        boolean flag = this.systemSettingsService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getSystemSettingsId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( SystemSettingsVO vo ) {
                SystemSettings entity = this.systemSettingsService.getById(vo.getSystemSettingsId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( SystemSettingsVO vo) {
                boolean flag = this.systemSettingsService.removeById(vo.getSystemSettingsId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
