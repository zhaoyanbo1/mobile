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
import com.kuafu.web.entity.ActivityRecommendation;
import com.kuafu.web.service.IActivityRecommendationService;
import com.kuafu.web.vo.ActivityRecommendationPageVO;
import com.kuafu.web.vo.ActivityRecommendationVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 活动推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("ActivityRecommendationControllerService")
@RequiredArgsConstructor
public class ActivityRecommendationControllerService  {

    private final IActivityRecommendationService activityRecommendationService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ActivityRecommendationPageVO pageVO){
        IPage<ActivityRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ActivityRecommendation> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ActivityRecommendation::getActivityRecommendationId);

            if(pageVO.getActivityRecommendationId() != null){
                queryWrapper.eq(ActivityRecommendation::getActivityRecommendationId, pageVO.getActivityRecommendationId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.eq(ActivityRecommendation::getTitle, pageVO.getTitle());
            }


            if(pageVO.getActivityTime() != null){
                queryWrapper.eq(ActivityRecommendation::getActivityTime, pageVO.getActivityTime());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationLatitude())) {
                queryWrapper.eq(ActivityRecommendation::getLocationLatitude, pageVO.getLocationLatitude());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationLongitude())) {
                queryWrapper.eq(ActivityRecommendation::getLocationLongitude, pageVO.getLocationLongitude());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationAddress())) {
                queryWrapper.eq(ActivityRecommendation::getLocationAddress, pageVO.getLocationAddress());
            }


        if(StringUtils.isNotEmpty(pageVO.getSuitablePeople())) {
                queryWrapper.eq(ActivityRecommendation::getSuitablePeople, pageVO.getSuitablePeople());
            }


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(ActivityRecommendation::getCreationTime, pageVO.getCreationTime());
            }

            return ResultUtils.success(activityRecommendationService.page(page, queryWrapper));
        }


        public BaseResponse list(  ActivityRecommendationVO vo){
            LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(ActivityRecommendation::getTitle, vo.getTitle());
                }
                if(vo.getActivityTime() != null){
                    queryWrapper.eq(ActivityRecommendation::getActivityTime, vo.getActivityTime());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
                    queryWrapper.eq(ActivityRecommendation::getLocationLatitude, vo.getLocationLatitude());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
                    queryWrapper.eq(ActivityRecommendation::getLocationLongitude, vo.getLocationLongitude());
                }
            if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
                    queryWrapper.eq(ActivityRecommendation::getLocationAddress, vo.getLocationAddress());
                }
            if(StringUtils.isNotEmpty(vo.getSuitablePeople())) {
                    queryWrapper.eq(ActivityRecommendation::getSuitablePeople, vo.getSuitablePeople());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(ActivityRecommendation::getCreationTime, vo.getCreationTime());
                }
                return ResultUtils.success(activityRecommendationService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ActivityRecommendationVO vo) {
                ActivityRecommendation entity =  ActivityRecommendation.builder()
                    .title(vo.getTitle())
                    .activityTime(vo.getActivityTime())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .suitablePeople(vo.getSuitablePeople())
                    .creationTime(vo.getCreationTime())
                .build();








                boolean flag =this.activityRecommendationService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("activity_recommendation").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getActivityRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ActivityRecommendationVO vo) {
                ActivityRecommendation entity =  ActivityRecommendation.builder()
                    .activityRecommendationId(vo.getActivityRecommendationId())
                    .title(vo.getTitle())
                    .activityTime(vo.getActivityTime())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .suitablePeople(vo.getSuitablePeople())
                    .creationTime(vo.getCreationTime())
                .build();



                        boolean flag = this.activityRecommendationService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getActivityRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ActivityRecommendationVO vo ) {
                ActivityRecommendation entity = this.activityRecommendationService.getById(vo.getActivityRecommendationId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ActivityRecommendationVO vo) {
                boolean flag = this.activityRecommendationService.removeById(vo.getActivityRecommendationId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
