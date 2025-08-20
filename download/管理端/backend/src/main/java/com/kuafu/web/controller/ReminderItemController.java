package com.kuafu.web.controller;

import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import com.kuafu.common.util.excel.ExcelProvider;
import org.apache.poi.ss.usermodel.Workbook;
import com.kuafu.common.file.FileUploadUtils;
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
import org.springframework.web.bind.annotation.RequestPart;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.entity.SelectVO;
import com.kuafu.common.util.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import com.kuafu.common.sensitive.SensitiveFilter;
import com.kuafu.common.domin.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IReminderItemService;
import com.kuafu.web.vo.ReminderItemPageVO;
import com.kuafu.web.vo.ReminderItemVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 提醒事项 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminderItem")
@Api(tags = {"提醒事项"})
public class ReminderItemController  {

    private final IReminderItemService reminderItemService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ReminderItemPageVO pageVO){
        IPage<ReminderItem> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

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
            if(StringUtils.isNotEmpty(pageVO.getMedicinePhotoResourceKey())) {
            queryWrapper.eq(ReminderItem::getMedicinePhotoResourceKey, pageVO.getMedicinePhotoResourceKey());
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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ReminderItemVO vo){
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
            if(StringUtils.isNotEmpty(vo.getMedicinePhotoResourceKey())) {
            queryWrapper.eq(ReminderItem::getMedicinePhotoResourceKey, vo.getMedicinePhotoResourceKey());
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ReminderItem::getReminderItemId);

        List<ReminderItem> list =reminderItemService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getReminderItemId(), l.getReminderItemId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ReminderItemVO vo) {
        ReminderItem entity =  ReminderItem.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .title(vo.getTitle())
                    .description(vo.getDescription())
                    .reminderTime(vo.getReminderTime())
                    .isCompleted(vo.getIsCompleted())
                  .medicinePhotoResourceKey(vo.getMedicinePhotoResourceKey() == null ? "default_resource_key" : vo.getMedicinePhotoResourceKey())
                    .medicineDosage(vo.getMedicineDosage())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .dietRecipeId(vo.getDietRecipeId())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag =this.reminderItemService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("reminderItem").data(entity).build());
                }

                final List<StaticResource> MedicinePhotoImage = vo.getMedicinePhoto();
                if (MedicinePhotoImage  != null && !MedicinePhotoImage.isEmpty()) {
                    MedicinePhotoImage.forEach(i -> {
                        i.setRelateTableColumnName("medicine_photo");
                        i.setRelatedTableName("reminder_item");
                        i.setRelatedTableKey(entity.getReminderItemId());
                        i.setResourcePath(i.getResourceName());
                    });

                    staticResourceService.saveBatch(MedicinePhotoImage);

                }

        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ReminderItemVO vo) {
        ReminderItem entity =  ReminderItem.builder()
                    .reminderItemId(vo.getReminderItemId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .title(vo.getTitle())
                    .description(vo.getDescription())
                    .reminderTime(vo.getReminderTime())
                    .isCompleted(vo.getIsCompleted())
                    .medicinePhotoResourceKey(vo.getMedicinePhotoResourceKey())
                    .medicineDosage(vo.getMedicineDosage())
                    .locationLatitude(vo.getLocationLatitude())
                    .locationLongitude(vo.getLocationLongitude())
                    .locationAddress(vo.getLocationAddress())
                    .dietRecipeId(vo.getDietRecipeId())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag = this.reminderItemService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("reminderItem").data(entity).build());
                }

                final LambdaQueryWrapper<StaticResource> medicine_photoqueryWrapper = new LambdaQueryWrapper<>();
                medicine_photoqueryWrapper
                        .eq(StaticResource::getRelatedTableName,"reminder_item")
                        .eq(StaticResource::getRelateTableColumnName,"medicine_photo")
                        .eq(StaticResource::getRelatedTableKey,entity.getReminderItemId());
                staticResourceService.remove(medicine_photoqueryWrapper);


                final List<StaticResource> MedicinePhotoImage = vo.getMedicinePhoto();
                if (MedicinePhotoImage  != null && !MedicinePhotoImage.isEmpty()) {
                    MedicinePhotoImage.forEach(i -> {
                        i.setRelateTableColumnName("medicine_photo");
                        i.setRelatedTableName("reminder_item");
                        i.setRelatedTableKey(entity.getReminderItemId());
                        if(StringUtils.isNotEmpty(i.getResourceName())) {
                            i.setResourcePath(i.getResourceName());
                        }
                    });

                    staticResourceService.saveBatch(MedicinePhotoImage);

                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer reminderItemId) {
        ReminderItem entity = this.reminderItemService.getById(reminderItemId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer reminderItemId) {
        boolean flag = this.reminderItemService.removeById(reminderItemId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("reminderItem").data( ReminderItem.builder().reminderItemId(reminderItemId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ReminderItemPageVO pageVO) {

        LambdaQueryWrapper<ReminderItem> queryWrapper = new LambdaQueryWrapper<>();

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
                    if(StringUtils.isNotEmpty(pageVO.getMedicinePhotoResourceKey())) {
                            queryWrapper.eq(ReminderItem::getMedicinePhotoResourceKey, pageVO.getMedicinePhotoResourceKey());
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
            List<ReminderItem> list = reminderItemService.list(queryWrapper);
            ExcelUtil<ReminderItem> util = new ExcelUtil<>(ReminderItem.class);
            util.exportExcel(response, list, "数据");
        }

    /**
     * 导入excel
     * @param file
     * @return
     */
    @PostMapping("/import")
    public BaseResponse importExcel(@RequestPart(name = "file") MultipartFile file)
    {
        String extension = FileUploadUtils.getExtension(file);
        if (StringUtils.equalsIgnoreCase(extension, "pdf")) {
            excelProvider.pdfData(file, ReminderItem.class, reminderItemService::saveBatch);
        }
        else{
            excelProvider.importData(file, ReminderItem.class, reminderItemService::saveBatch);
        }
            return ResultUtils.success("导入成功");
    }

    /**
     * 下载 excel 模版
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
                    try {
                        Workbook workbook = excelProvider.downloadExcelTemplate(ReminderItem.class);
                        // 返回文件流
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setCharacterEncoding("utf-8");
                        workbook.write(response.getOutputStream());
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    }


            }
