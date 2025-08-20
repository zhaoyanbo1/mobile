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
import com.kuafu.web.entity.SystemSettings;
import com.kuafu.web.service.ISystemSettingsService;
import com.kuafu.web.vo.SystemSettingsPageVO;
import com.kuafu.web.vo.SystemSettingsVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 系统设置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/systemSettings")
@Api(tags = {"系统设置"})
public class SystemSettingsController  {

    private final ISystemSettingsService systemSettingsService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody SystemSettingsPageVO pageVO){
        IPage<SystemSettings> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody SystemSettingsVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(SystemSettings::getSystemSettingsId);

        List<SystemSettings> list =systemSettingsService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getSystemSettingsId(), l.getSystemSettingsId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody SystemSettingsVO vo) {
        SystemSettings entity =  SystemSettings.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderVolume(vo.getReminderVolume())
                    .fontSize(vo.getFontSize())
                    .questionnaireExported(vo.getQuestionnaireExported())
                .build();
        boolean flag =this.systemSettingsService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("systemSettings").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody SystemSettingsVO vo) {
        SystemSettings entity =  SystemSettings.builder()
                    .systemSettingsId(vo.getSystemSettingsId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .reminderVolume(vo.getReminderVolume())
                    .fontSize(vo.getFontSize())
                    .questionnaireExported(vo.getQuestionnaireExported())
                .build();
        boolean flag = this.systemSettingsService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("systemSettings").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer systemSettingsId) {
        SystemSettings entity = this.systemSettingsService.getById(systemSettingsId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer systemSettingsId) {
        boolean flag = this.systemSettingsService.removeById(systemSettingsId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("systemSettings").data( SystemSettings.builder().systemSettingsId(systemSettingsId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SystemSettingsPageVO pageVO) {

        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();

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
            List<SystemSettings> list = systemSettingsService.list(queryWrapper);
            ExcelUtil<SystemSettings> util = new ExcelUtil<>(SystemSettings.class);
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
            excelProvider.pdfData(file, SystemSettings.class, systemSettingsService::saveBatch);
        }
        else{
            excelProvider.importData(file, SystemSettings.class, systemSettingsService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(SystemSettings.class);
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
