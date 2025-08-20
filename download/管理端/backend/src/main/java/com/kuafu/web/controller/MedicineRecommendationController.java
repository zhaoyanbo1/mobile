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
import com.kuafu.web.entity.MedicineRecommendation;
import com.kuafu.web.service.IMedicineRecommendationService;
import com.kuafu.web.vo.MedicineRecommendationPageVO;
import com.kuafu.web.vo.MedicineRecommendationVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 药品推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/medicineRecommendation")
@Api(tags = {"药品推荐"})
public class MedicineRecommendationController  {

    private final IMedicineRecommendationService medicineRecommendationService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody MedicineRecommendationPageVO pageVO){
        IPage<MedicineRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody MedicineRecommendationVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(MedicineRecommendation::getMedicineRecommendationId);

        List<MedicineRecommendation> list =medicineRecommendationService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getMedicineRecommendationId(), l.getMedicineRecommendationId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody MedicineRecommendationVO vo) {
        MedicineRecommendation entity =  MedicineRecommendation.builder()
                    .title(vo.getTitle())
                    .usageGuide(vo.getUsageGuide())
                    .nearbyPharmacyInfo(vo.getNearbyPharmacyInfo())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag =this.medicineRecommendationService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("medicineRecommendation").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody MedicineRecommendationVO vo) {
        MedicineRecommendation entity =  MedicineRecommendation.builder()
                    .medicineRecommendationId(vo.getMedicineRecommendationId())
                    .title(vo.getTitle())
                    .usageGuide(vo.getUsageGuide())
                    .nearbyPharmacyInfo(vo.getNearbyPharmacyInfo())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag = this.medicineRecommendationService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("medicineRecommendation").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer medicineRecommendationId) {
        MedicineRecommendation entity = this.medicineRecommendationService.getById(medicineRecommendationId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer medicineRecommendationId) {
        boolean flag = this.medicineRecommendationService.removeById(medicineRecommendationId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("medicineRecommendation").data( MedicineRecommendation.builder().medicineRecommendationId(medicineRecommendationId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody MedicineRecommendationPageVO pageVO) {

        LambdaQueryWrapper<MedicineRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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
            List<MedicineRecommendation> list = medicineRecommendationService.list(queryWrapper);
            ExcelUtil<MedicineRecommendation> util = new ExcelUtil<>(MedicineRecommendation.class);
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
            excelProvider.pdfData(file, MedicineRecommendation.class, medicineRecommendationService::saveBatch);
        }
        else{
            excelProvider.importData(file, MedicineRecommendation.class, medicineRecommendationService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(MedicineRecommendation.class);
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
