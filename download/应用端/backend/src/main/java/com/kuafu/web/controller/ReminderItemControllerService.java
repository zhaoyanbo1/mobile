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
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IReminderItemService;
import com.kuafu.web.vo.ReminderItemPageVO;
import com.kuafu.web.vo.ReminderItemVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 提醒事项 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Service("ReminderItemControllerService")
@RequiredArgsConstructor
public class ReminderItemControllerService  {

    private final IReminderItemService reminderItemService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ReminderItemPageVO pageVO){
        IPage<ReminderItem> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ReminderItem> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ReminderItem> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ReminderItem::getReminderItemId);

            if(pageVO.getReminderItemId() != null){
                queryWrapper.eq(ReminderItem::getReminderItemId, pageVO.getReminderItemId());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(ReminderItem::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


            if(pageVO.getReminderTypeEnumId() != null){
                queryWrapper.eq(ReminderItem::getReminderTypeEnumId, pageVO.getReminderTypeEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.eq(ReminderItem::getTitle, pageVO.getTitle());
            }


        if(StringUtils.isNotEmpty(pageVO.getDescription())) {
                queryWrapper.eq(ReminderItem::getDescription, pageVO.getDescription());
            }


            if(pageVO.getReminderTime() != null){
                queryWrapper.eq(ReminderItem::getReminderTime, pageVO.getReminderTime());
            }


            if(pageVO.getIsCompleted() != null){
                queryWrapper.eq(ReminderItem::getIsCompleted, pageVO.getIsCompleted());
            }


        if(StringUtils.isNotEmpty(pageVO.getMedicineDosage())) {
                queryWrapper.eq(ReminderItem::getMedicineDosage, pageVO.getMedicineDosage());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationLatitude())) {
                queryWrapper.eq(ReminderItem::getLocationLatitude, pageVO.getLocationLatitude());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationLongitude())) {
                queryWrapper.eq(ReminderItem::getLocationLongitude, pageVO.getLocationLongitude());
            }


        if(StringUtils.isNotEmpty(pageVO.getLocationAddress())) {
                queryWrapper.eq(ReminderItem::getLocationAddress, pageVO.getLocationAddress());
            }


            if(pageVO.getDietRecipeId() != null){
                queryWrapper.eq(ReminderItem::getDietRecipeId, pageVO.getDietRecipeId());
            }


            if(pageVO.getCreationTime() != null){
                queryWrapper.eq(ReminderItem::getCreationTime, pageVO.getCreationTime());
            }


            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(ReminderItem::getUpdateTime, pageVO.getUpdateTime());
            }

            return ResultUtils.success(reminderItemService.page(page, queryWrapper));
        }


        public BaseResponse list(  ReminderItemVO vo){
            LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(ReminderItem::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getReminderTypeEnumId() != null){
                    queryWrapper.eq(ReminderItem::getReminderTypeEnumId, vo.getReminderTypeEnumId());
                }
            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(ReminderItem::getTitle, vo.getTitle());
                }
            if(StringUtils.isNotEmpty(vo.getDescription())) {
                    queryWrapper.eq(ReminderItem::getDescription, vo.getDescription());
                }
                if(vo.getReminderTime() != null){
                    queryWrapper.eq(ReminderItem::getReminderTime, vo.getReminderTime());
                }
                if(vo.getIsCompleted() != null){
                    queryWrapper.eq(ReminderItem::getIsCompleted, vo.getIsCompleted());
                }
            if(StringUtils.isNotEmpty(vo.getMedicineDosage())) {
                    queryWrapper.eq(ReminderItem::getMedicineDosage, vo.getMedicineDosage());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLatitude())) {
                    queryWrapper.eq(ReminderItem::getLocationLatitude, vo.getLocationLatitude());
                }
            if(StringUtils.isNotEmpty(vo.getLocationLongitude())) {
                    queryWrapper.eq(ReminderItem::getLocationLongitude, vo.getLocationLongitude());
                }
            if(StringUtils.isNotEmpty(vo.getLocationAddress())) {
                    queryWrapper.eq(ReminderItem::getLocationAddress, vo.getLocationAddress());
                }
                if(vo.getDietRecipeId() != null){
                    queryWrapper.eq(ReminderItem::getDietRecipeId, vo.getDietRecipeId());
                }
                if(vo.getCreationTime() != null){
                    queryWrapper.eq(ReminderItem::getCreationTime, vo.getCreationTime());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(ReminderItem::getUpdateTime, vo.getUpdateTime());
                }
                return ResultUtils.success(reminderItemService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ReminderItemVO vo) {
                ReminderItem entity =  ReminderItem.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .title(vo.getTitle())
                    .description(vo.getDescription())
                    .reminderTime(vo.getReminderTime())
                    .isCompleted(vo.getIsCompleted())
                    .medicineDosage(vo.getMedicineDosage())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .dietRecipeId(vo.getDietRecipeId())
                    .creationTime(vo.getCreationTime())
                .build();







                boolean  f = transactionTemplate.execute(status -> {
                    try {

                boolean flag =this.reminderItemService.save(entity);


                final List<StaticResource> MedicinePhotoImage = vo.getMedicinePhoto();
                if (MedicinePhotoImage  != null && !MedicinePhotoImage .isEmpty()) {
                    MedicinePhotoImage.forEach(i -> {
                        i.setRelateTableColumnName("medicine_photo");
                        i.setRelatedTableName("reminder_item");
                        i.setResourceId(null);
                        i.setRelatedTableKey(entity.getReminderItemId());
                    });
                }
                staticResourceService.saveBatch(MedicinePhotoImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                if(
                f
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("reminder_item").data(entity).build());
                }

                return
                                f
                              
                ? ResultUtils.success(entity.getReminderItemId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ReminderItemVO vo) {
                ReminderItem entity =  ReminderItem.builder()
                    .reminderItemId(vo.getReminderItemId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .title(vo.getTitle())
                    .description(vo.getDescription())
                    .reminderTime(vo.getReminderTime())
                    .isCompleted(vo.getIsCompleted())
                    .medicineDosage(vo.getMedicineDosage())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .dietRecipeId(vo.getDietRecipeId())
                    .creationTime(vo.getCreationTime())
                .build();


                boolean  f = transactionTemplate.execute(status -> {
                    try {

                        boolean flag = this.reminderItemService.saveOrUpdate(entity);



                        final List<StaticResource> MedicinePhotoImage = vo.getMedicinePhoto();

                        if (MedicinePhotoImage  != null) {
                        final LambdaQueryWrapper<StaticResource> medicine_photoqueryWrapper = new LambdaQueryWrapper<>();
                        medicine_photoqueryWrapper.eq(StaticResource::getRelatedTableName,"reminder_item").eq(StaticResource::getRelateTableColumnName,"medicine_photo").eq(StaticResource::getRelatedTableKey,entity.getReminderItemId());
                        staticResourceService.remove(medicine_photoqueryWrapper);
                        }


                        if (MedicinePhotoImage  != null && !MedicinePhotoImage .isEmpty()) {
                            MedicinePhotoImage.forEach(i -> {
                                i.setRelateTableColumnName("medicine_photo");
                                i.setRelatedTableName("reminder_item");
                                i.setRelatedTableKey(entity.getReminderItemId());
                            });
                        }
                        staticResourceService.saveBatch(MedicinePhotoImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                return  
                f
                         ? ResultUtils.success(entity.getReminderItemId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ReminderItemVO vo ) {
                ReminderItem entity = this.reminderItemService.getById(vo.getReminderItemId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ReminderItemVO vo) {
                boolean flag = this.reminderItemService.removeById(vo.getReminderItemId());




                final LambdaQueryWrapper<StaticResource> medicine_photoqueryWrapper = new LambdaQueryWrapper<>();
                medicine_photoqueryWrapper.eq(StaticResource::getRelatedTableName,"reminder_item").eq(StaticResource::getRelateTableColumnName,"medicine_photo").eq(StaticResource::getRelatedTableKey,vo.getReminderItemId());
                staticResourceService.remove(medicine_photoqueryWrapper);


                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
