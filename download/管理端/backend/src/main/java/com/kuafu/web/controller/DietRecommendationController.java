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
import com.kuafu.web.entity.DietRecommendation;
import com.kuafu.web.service.IDietRecommendationService;
import com.kuafu.web.vo.DietRecommendationPageVO;
import com.kuafu.web.vo.DietRecommendationVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 饮食推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dietRecommendation")
@Api(tags = {"饮食推荐"})
public class DietRecommendationController  {

    private final IDietRecommendationService dietRecommendationService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody DietRecommendationPageVO pageVO){
        IPage<DietRecommendation> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody DietRecommendationVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(DietRecommendation::getDietRecommendationId);

        List<DietRecommendation> list =dietRecommendationService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getDietRecommendationId(), l.getDietRecommendationId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody DietRecommendationVO vo) {
        DietRecommendation entity =  DietRecommendation.builder()
                    .title(vo.getTitle())
                    .difficulty(vo.getDifficulty())
                    .requiredTime(vo.getRequiredTime())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag =this.dietRecommendationService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("dietRecommendation").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody DietRecommendationVO vo) {
        DietRecommendation entity =  DietRecommendation.builder()
                    .dietRecommendationId(vo.getDietRecommendationId())
                    .title(vo.getTitle())
                    .difficulty(vo.getDifficulty())
                    .requiredTime(vo.getRequiredTime())
                    .creationTime(vo.getCreationTime())
                .build();
        boolean flag = this.dietRecommendationService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("dietRecommendation").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer dietRecommendationId) {
        DietRecommendation entity = this.dietRecommendationService.getById(dietRecommendationId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer dietRecommendationId) {
        boolean flag = this.dietRecommendationService.removeById(dietRecommendationId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("dietRecommendation").data( DietRecommendation.builder().dietRecommendationId(dietRecommendationId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody DietRecommendationPageVO pageVO) {

        LambdaQueryWrapper<DietRecommendation> queryWrapper = new LambdaQueryWrapper<>();

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
            List<DietRecommendation> list = dietRecommendationService.list(queryWrapper);
            ExcelUtil<DietRecommendation> util = new ExcelUtil<>(DietRecommendation.class);
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
            excelProvider.pdfData(file, DietRecommendation.class, dietRecommendationService::saveBatch);
        }
        else{
            excelProvider.importData(file, DietRecommendation.class, dietRecommendationService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(DietRecommendation.class);
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
