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
import com.kuafu.web.entity.ActivityRecommendation;
import com.kuafu.web.service.IActivityRecommendationService;
import com.kuafu.web.vo.ActivityRecommendationPageVO;
import com.kuafu.web.vo.ActivityRecommendationVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 活动推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/activityRecommendation")
@Api(tags = {"活动推荐"})
public class ActivityRecommendationController  {

    private final IActivityRecommendationService activityRecommendationService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ActivityRecommendationPageVO pageVO){
        IPage<ActivityRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ActivityRecommendationVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ActivityRecommendation::getActivityRecommendationId);

        List<ActivityRecommendation> list =activityRecommendationService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getActivityRecommendationId(), l.getActivityRecommendationId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ActivityRecommendationVO vo) {
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
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("activityRecommendation").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ActivityRecommendationVO vo) {
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
        boolean flag = this.activityRecommendationService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("activityRecommendation").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer activityRecommendationId) {
        ActivityRecommendation entity = this.activityRecommendationService.getById(activityRecommendationId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer activityRecommendationId) {
        boolean flag = this.activityRecommendationService.removeById(activityRecommendationId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("activityRecommendation").data( ActivityRecommendation.builder().activityRecommendationId(activityRecommendationId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ActivityRecommendationPageVO pageVO) {

        LambdaQueryWrapper<ActivityRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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
            List<ActivityRecommendation> list = activityRecommendationService.list(queryWrapper);
            ExcelUtil<ActivityRecommendation> util = new ExcelUtil<>(ActivityRecommendation.class);
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
            excelProvider.pdfData(file, ActivityRecommendation.class, activityRecommendationService::saveBatch);
        }
        else{
            excelProvider.importData(file, ActivityRecommendation.class, activityRecommendationService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(ActivityRecommendation.class);
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
