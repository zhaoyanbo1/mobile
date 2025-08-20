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
import com.kuafu.web.entity.MedicineRecommendation;
import com.kuafu.web.service.IMedicineRecommendationService;
import com.kuafu.web.vo.MedicineRecommendationPageVO;
import com.kuafu.web.vo.MedicineRecommendationVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 药品推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("MedicineRecommendationControllerService")
@RequiredArgsConstructor
public class MedicineRecommendationControllerService  {

    private final IMedicineRecommendationService medicineRecommendationService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( MedicineRecommendationPageVO pageVO){
        IPage<MedicineRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<MedicineRecommendation> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(MedicineRecommendation::getMedicineRecommendationId);

            if(pageVO.getMedicineRecommendationId() != null){
                queryWrapper.eq(MedicineRecommendation::getMedicineRecommendationId, pageVO.getMedicineRecommendationId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.eq(MedicineRecommendation::getTitle, pageVO.getTitle());
            }


        if(StringUtils.isNotEmpty(pageVO.getUsageGuide())) {
                queryWrapper.eq(MedicineRecommendation::getUsageGuide, pageVO.getUsageGuide());
            }


        if(StringUtils.isNotEmpty(pageVO.getNearbyPharmacyInfo())) {
                queryWrapper.eq(MedicineRecommendation::getNearbyPharmacyInfo, pageVO.getNearbyPharmacyInfo());
            }


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(MedicineRecommendation::getCreationTime, pageVO.getCreationTime());
            }

            return ResultUtils.success(medicineRecommendationService.page(page, queryWrapper));
        }


        public BaseResponse list(  MedicineRecommendationVO vo){
            LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(MedicineRecommendation::getTitle, vo.getTitle());
                }
            if(StringUtils.isNotEmpty(vo.getUsageGuide())) {
                    queryWrapper.eq(MedicineRecommendation::getUsageGuide, vo.getUsageGuide());
                }
            if(StringUtils.isNotEmpty(vo.getNearbyPharmacyInfo())) {
                    queryWrapper.eq(MedicineRecommendation::getNearbyPharmacyInfo, vo.getNearbyPharmacyInfo());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(MedicineRecommendation::getCreationTime, vo.getCreationTime());
                }
                return ResultUtils.success(medicineRecommendationService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  MedicineRecommendationVO vo) {
                MedicineRecommendation entity =  MedicineRecommendation.builder()
                    .title(vo.getTitle())
                    .usageGuide(vo.getUsageGuide())
                    .nearbyPharmacyInfo(vo.getNearbyPharmacyInfo())
                    .creationTime(vo.getCreationTime())
                .build();








                boolean flag =this.medicineRecommendationService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("medicine_recommendation").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getMedicineRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( MedicineRecommendationVO vo) {
                MedicineRecommendation entity =  MedicineRecommendation.builder()
                    .medicineRecommendationId(vo.getMedicineRecommendationId())
                    .title(vo.getTitle())
                    .usageGuide(vo.getUsageGuide())
                    .nearbyPharmacyInfo(vo.getNearbyPharmacyInfo())
                    .creationTime(vo.getCreationTime())
                .build();



                        boolean flag = this.medicineRecommendationService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getMedicineRecommendationId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( MedicineRecommendationVO vo ) {
                MedicineRecommendation entity = this.medicineRecommendationService.getById(vo.getMedicineRecommendationId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( MedicineRecommendationVO vo) {
                boolean flag = this.medicineRecommendationService.removeById(vo.getMedicineRecommendationId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
