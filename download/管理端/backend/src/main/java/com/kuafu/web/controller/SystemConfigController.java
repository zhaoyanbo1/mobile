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
import com.kuafu.web.entity.SystemConfig;
import com.kuafu.web.service.ISystemConfigService;
import com.kuafu.web.vo.SystemConfigPageVO;
import com.kuafu.web.vo.SystemConfigVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 系统配置 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/systemConfig")
@Api(tags = {"系统配置"})
public class SystemConfigController  {

    private final ISystemConfigService systemConfigService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody SystemConfigPageVO pageVO){
        IPage<SystemConfig> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getName())) {
            queryWrapper.like(SystemConfig::getName, pageVO.getName());
        }
            if(StringUtils.isNotEmpty(pageVO.getChineseName())) {
            queryWrapper.like(SystemConfig::getChineseName, pageVO.getChineseName());
        }
            if(StringUtils.isNotEmpty(pageVO.getDescription())) {
            queryWrapper.eq(SystemConfig::getDescription, pageVO.getDescription());
        }
            if(StringUtils.isNotEmpty(pageVO.getContent())) {
            queryWrapper.eq(SystemConfig::getContent, pageVO.getContent());
        }
            if(StringUtils.isNotEmpty(pageVO.getRemark())) {
            queryWrapper.eq(SystemConfig::getRemark, pageVO.getRemark());
        }
            if(StringUtils.isNotEmpty(pageVO.getType())) {
            queryWrapper.eq(SystemConfig::getType, pageVO.getType());
        }
        return ResultUtils.success(systemConfigService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody SystemConfigVO vo){
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like(SystemConfig::getName, vo.getName());
        }
            if(StringUtils.isNotEmpty(vo.getChineseName())) {
            queryWrapper.like(SystemConfig::getChineseName, vo.getChineseName());
        }
            if(StringUtils.isNotEmpty(vo.getDescription())) {
            queryWrapper.eq(SystemConfig::getDescription, vo.getDescription());
        }
            if(StringUtils.isNotEmpty(vo.getContent())) {
            queryWrapper.eq(SystemConfig::getContent, vo.getContent());
        }
            if(StringUtils.isNotEmpty(vo.getRemark())) {
            queryWrapper.eq(SystemConfig::getRemark, vo.getRemark());
        }
            if(StringUtils.isNotEmpty(vo.getType())) {
            queryWrapper.eq(SystemConfig::getType, vo.getType());
        }
        return ResultUtils.success(systemConfigService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(SystemConfig::getId);

        List<SystemConfig> list =systemConfigService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getId(), l.getId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody SystemConfigVO vo) {
        SystemConfig entity =  SystemConfig.builder()
                    .name(vo.getName())
                    .chineseName(vo.getChineseName())
                    .description(vo.getDescription())
                    .content(vo.getContent())
                    .remark(vo.getRemark())
                    .type(vo.getType())
                .build();
        boolean flag =this.systemConfigService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("systemConfig").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody SystemConfigVO vo) {
        SystemConfig entity =  SystemConfig.builder()
                    .id(vo.getId())
                    .name(vo.getName())
                    .chineseName(vo.getChineseName())
                    .description(vo.getDescription())
                    .content(vo.getContent())
                    .remark(vo.getRemark())
                    .type(vo.getType())
                .build();
        boolean flag = this.systemConfigService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("systemConfig").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer id) {
        SystemConfig entity = this.systemConfigService.getById(id);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer id) {
        boolean flag = this.systemConfigService.removeById(id);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("systemConfig").data( SystemConfig.builder().id(id).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SystemConfigPageVO pageVO) {

        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getName())) {
                                    queryWrapper.like(SystemConfig::getName, pageVO.getName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getChineseName())) {
                                    queryWrapper.like(SystemConfig::getChineseName, pageVO.getChineseName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getDescription())) {
                            queryWrapper.eq(SystemConfig::getDescription, pageVO.getDescription());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getContent())) {
                            queryWrapper.eq(SystemConfig::getContent, pageVO.getContent());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getRemark())) {
                            queryWrapper.eq(SystemConfig::getRemark, pageVO.getRemark());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getType())) {
                            queryWrapper.eq(SystemConfig::getType, pageVO.getType());
                        }
            List<SystemConfig> list = systemConfigService.list(queryWrapper);
            ExcelUtil<SystemConfig> util = new ExcelUtil<>(SystemConfig.class);
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
            excelProvider.pdfData(file, SystemConfig.class, systemConfigService::saveBatch);
        }
        else{
            excelProvider.importData(file, SystemConfig.class, systemConfigService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(SystemConfig.class);
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
