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
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.service.IHealthQuestionnaireService;
import com.kuafu.web.vo.HealthQuestionnairePageVO;
import com.kuafu.web.vo.HealthQuestionnaireVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 健康问卷 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/healthQuestionnaire")
@Api(tags = {"健康问卷"})
public class HealthQuestionnaireController  {

    private final IHealthQuestionnaireService healthQuestionnaireService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody HealthQuestionnairePageVO pageVO){
        IPage<HealthQuestionnaire> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getName())) {
            queryWrapper.like(HealthQuestionnaire::getName, pageVO.getName());
        }
            if(pageVO.getAge() != null){
            queryWrapper.eq(HealthQuestionnaire::getAge, pageVO.getAge());
        }
            if(StringUtils.isNotEmpty(pageVO.getResidence())) {
            queryWrapper.eq(HealthQuestionnaire::getResidence, pageVO.getResidence());
        }
            if(StringUtils.isNotEmpty(pageVO.getChronicDisease())) {
            queryWrapper.eq(HealthQuestionnaire::getChronicDisease, pageVO.getChronicDisease());
        }
            if(StringUtils.isNotEmpty(pageVO.getAllergyHistory())) {
            queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, pageVO.getAllergyHistory());
        }
            if(StringUtils.isNotEmpty(pageVO.getCommonMedication())) {
            queryWrapper.eq(HealthQuestionnaire::getCommonMedication, pageVO.getCommonMedication());
        }
            if(StringUtils.isNotEmpty(pageVO.getDietPreference())) {
            queryWrapper.eq(HealthQuestionnaire::getDietPreference, pageVO.getDietPreference());
        }
            if(StringUtils.isNotEmpty(pageVO.getExerciseFrequency())) {
            queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, pageVO.getExerciseFrequency());
        }
            if(pageVO.getCreationTime() != null){
            queryWrapper.eq(HealthQuestionnaire::getCreationTime, pageVO.getCreationTime());
        }
            if(pageVO.getUpdateTime() != null){
            queryWrapper.eq(HealthQuestionnaire::getUpdateTime, pageVO.getUpdateTime());
        }
            if(pageVO.getVersion() != null){
            queryWrapper.eq(HealthQuestionnaire::getVersion, pageVO.getVersion());
        }
        return ResultUtils.success(healthQuestionnaireService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody HealthQuestionnaireVO vo){
        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like(HealthQuestionnaire::getName, vo.getName());
        }
                if(vo.getAge() != null){
            queryWrapper.eq(HealthQuestionnaire::getAge, vo.getAge());
        }
            if(StringUtils.isNotEmpty(vo.getResidence())) {
            queryWrapper.eq(HealthQuestionnaire::getResidence, vo.getResidence());
        }
            if(StringUtils.isNotEmpty(vo.getChronicDisease())) {
            queryWrapper.eq(HealthQuestionnaire::getChronicDisease, vo.getChronicDisease());
        }
            if(StringUtils.isNotEmpty(vo.getAllergyHistory())) {
            queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, vo.getAllergyHistory());
        }
            if(StringUtils.isNotEmpty(vo.getCommonMedication())) {
            queryWrapper.eq(HealthQuestionnaire::getCommonMedication, vo.getCommonMedication());
        }
            if(StringUtils.isNotEmpty(vo.getDietPreference())) {
            queryWrapper.eq(HealthQuestionnaire::getDietPreference, vo.getDietPreference());
        }
            if(StringUtils.isNotEmpty(vo.getExerciseFrequency())) {
            queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, vo.getExerciseFrequency());
        }
                if(vo.getCreationTime() != null){
            queryWrapper.eq(HealthQuestionnaire::getCreationTime, vo.getCreationTime());
        }
                if(vo.getUpdateTime() != null){
            queryWrapper.eq(HealthQuestionnaire::getUpdateTime, vo.getUpdateTime());
        }
                if(vo.getVersion() != null){
            queryWrapper.eq(HealthQuestionnaire::getVersion, vo.getVersion());
        }
        return ResultUtils.success(healthQuestionnaireService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(HealthQuestionnaire::getHealthQuestionnaireId);

        List<HealthQuestionnaire> list =healthQuestionnaireService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getHealthQuestionnaireId(), l.getHealthQuestionnaireId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody HealthQuestionnaireVO vo) {
        HealthQuestionnaire entity =  HealthQuestionnaire.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .age(vo.getAge())
                    .residence(vo.getResidence())
                    .chronicDisease(vo.getChronicDisease())
                    .allergyHistory(vo.getAllergyHistory())
                    .commonMedication(vo.getCommonMedication())
                    .dietPreference(vo.getDietPreference())
                    .exerciseFrequency(vo.getExerciseFrequency())
                    .creationTime(vo.getCreationTime())
                    .version(vo.getVersion())
                .build();
        boolean flag =this.healthQuestionnaireService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("healthQuestionnaire").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody HealthQuestionnaireVO vo) {
        HealthQuestionnaire entity =  HealthQuestionnaire.builder()
                    .healthQuestionnaireId(vo.getHealthQuestionnaireId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .age(vo.getAge())
                    .residence(vo.getResidence())
                    .chronicDisease(vo.getChronicDisease())
                    .allergyHistory(vo.getAllergyHistory())
                    .commonMedication(vo.getCommonMedication())
                    .dietPreference(vo.getDietPreference())
                    .exerciseFrequency(vo.getExerciseFrequency())
                    .creationTime(vo.getCreationTime())
                    .version(vo.getVersion())
                .build();
        boolean flag = this.healthQuestionnaireService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("healthQuestionnaire").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer healthQuestionnaireId) {
        HealthQuestionnaire entity = this.healthQuestionnaireService.getById(healthQuestionnaireId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer healthQuestionnaireId) {
        boolean flag = this.healthQuestionnaireService.removeById(healthQuestionnaireId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("healthQuestionnaire").data( HealthQuestionnaire.builder().healthQuestionnaireId(healthQuestionnaireId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody HealthQuestionnairePageVO pageVO) {

        LambdaQueryWrapper<HealthQuestionnaire> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getUserInfoUserInfoId1() != null){
                            queryWrapper.eq(HealthQuestionnaire::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getName())) {
                                    queryWrapper.like(HealthQuestionnaire::getName, pageVO.getName());
                        }
                        if(pageVO.getAge() != null){
                            queryWrapper.eq(HealthQuestionnaire::getAge, pageVO.getAge());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getResidence())) {
                            queryWrapper.eq(HealthQuestionnaire::getResidence, pageVO.getResidence());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getChronicDisease())) {
                            queryWrapper.eq(HealthQuestionnaire::getChronicDisease, pageVO.getChronicDisease());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getAllergyHistory())) {
                            queryWrapper.eq(HealthQuestionnaire::getAllergyHistory, pageVO.getAllergyHistory());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getCommonMedication())) {
                            queryWrapper.eq(HealthQuestionnaire::getCommonMedication, pageVO.getCommonMedication());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getDietPreference())) {
                            queryWrapper.eq(HealthQuestionnaire::getDietPreference, pageVO.getDietPreference());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getExerciseFrequency())) {
                            queryWrapper.eq(HealthQuestionnaire::getExerciseFrequency, pageVO.getExerciseFrequency());
                        }
                        if(pageVO.getCreationTime() != null){
                            queryWrapper.eq(HealthQuestionnaire::getCreationTime, pageVO.getCreationTime());
                        }
                        if(pageVO.getUpdateTime() != null){
                            queryWrapper.eq(HealthQuestionnaire::getUpdateTime, pageVO.getUpdateTime());
                        }
                        if(pageVO.getVersion() != null){
                            queryWrapper.eq(HealthQuestionnaire::getVersion, pageVO.getVersion());
                        }
            List<HealthQuestionnaire> list = healthQuestionnaireService.list(queryWrapper);
            ExcelUtil<HealthQuestionnaire> util = new ExcelUtil<>(HealthQuestionnaire.class);
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
            excelProvider.pdfData(file, HealthQuestionnaire.class, healthQuestionnaireService::saveBatch);
        }
        else{
            excelProvider.importData(file, HealthQuestionnaire.class, healthQuestionnaireService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(HealthQuestionnaire.class);
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
