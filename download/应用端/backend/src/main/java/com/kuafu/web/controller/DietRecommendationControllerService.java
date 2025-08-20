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
import com.kuafu.web.entity.DietRecommendation;
import com.kuafu.web.service.IDietRecommendationService;
import com.kuafu.web.vo.DietRecommendationPageVO;
import com.kuafu.web.vo.DietRecommendationVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 饮食推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("DietRecommendationControllerService")
@RequiredArgsConstructor
public class DietRecommendationControllerService  {

    private final IDietRecommendationService dietRecommendationService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( DietRecommendationPageVO pageVO){
        IPage<DietRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<DietRecommendation> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<DietRecommendation> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(DietRecommendation::getDietRecommendationId);

            if(pageVO.getDietRecommendationId() != null){
                queryWrapper.eq(DietRecommendation::getDietRecommendationId, pageVO.getDietRecommendationId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.eq(DietRecommendation::getTitle, pageVO.getTitle());
            }


        if(StringUtils.isNotEmpty(pageVO.getDifficulty())) {
                queryWrapper.eq(DietRecommendation::getDifficulty, pageVO.getDifficulty());
            }


        if(StringUtils.isNotEmpty(pageVO.getRequiredTime())) {
                queryWrapper.eq(DietRecommendation::getRequiredTime, pageVO.getRequiredTime());
            }


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(DietRecommendation::getCreationTime, pageVO.getCreationTime());
            }

            return ResultUtils.success(dietRecommendationService.page(page, queryWrapper));
        }


        public BaseResponse list(  DietRecommendationVO vo){
            LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(DietRecommendation::getTitle, vo.getTitle());
                }
            if(StringUtils.isNotEmpty(vo.getDifficulty())) {
                    queryWrapper.eq(DietRecommendation::getDifficulty, vo.getDifficulty());
                }
            if(StringUtils.isNotEmpty(vo.getRequiredTime())) {
                    queryWrapper.eq(DietRecommendation::getRequiredTime, vo.getRequiredTime());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(DietRecommendation::getCreationTime, vo.getCreationTime());
                }
                return ResultUtils.success(dietRecommendationService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  DietRecommendationVO vo) {
                DietRecommendation entity =  DietRecommendation.builder()
                    .title(vo.getTitle())
                    .difficulty(vo.getDifficulty())
                    .requiredTime(vo.getRequiredTime())
                    .creationTime(vo.getCreationTime())
                .build();








                boolean flag =this.dietRecommendationService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("diet_recommendation").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getDietRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( DietRecommendationVO vo) {
                DietRecommendation entity =  DietRecommendation.builder()
                    .dietRecommendationId(vo.getDietRecommendationId())
                    .title(vo.getTitle())
                    .difficulty(vo.getDifficulty())
                    .requiredTime(vo.getRequiredTime())
                    .creationTime(vo.getCreationTime())
                .build();



                        boolean flag = this.dietRecommendationService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getDietRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( DietRecommendationVO vo ) {
                DietRecommendation entity = this.dietRecommendationService.getById(vo.getDietRecommendationId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( DietRecommendationVO vo) {
                boolean flag = this.dietRecommendationService.removeById(vo.getDietRecommendationId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
