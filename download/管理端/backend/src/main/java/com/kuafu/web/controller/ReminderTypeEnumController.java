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
import com.kuafu.web.entity.ReminderTypeEnum;
import com.kuafu.web.service.IReminderTypeEnumService;
import com.kuafu.web.vo.ReminderTypeEnumPageVO;
import com.kuafu.web.vo.ReminderTypeEnumVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 提醒类型枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminderTypeEnum")
@Api(tags = {"提醒类型枚举"})
public class ReminderTypeEnumController  {

    private final IReminderTypeEnumService reminderTypeEnumService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ReminderTypeEnumPageVO pageVO){
        IPage<ReminderTypeEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getTypeName())) {
            queryWrapper.like(ReminderTypeEnum::getTypeName, pageVO.getTypeName());
        }
        return ResultUtils.success(reminderTypeEnumService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ReminderTypeEnumVO vo){
        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTypeName())) {
            queryWrapper.like(ReminderTypeEnum::getTypeName, vo.getTypeName());
        }
        return ResultUtils.success(reminderTypeEnumService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ReminderTypeEnum::getReminderTypeEnumId);

        List<ReminderTypeEnum> list =reminderTypeEnumService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getReminderTypeEnumId(), l.getReminderTypeEnumId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ReminderTypeEnumVO vo) {
        ReminderTypeEnum entity =  ReminderTypeEnum.builder()
                    .typeName(vo.getTypeName())
                .build();
        boolean flag =this.reminderTypeEnumService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("reminderTypeEnum").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ReminderTypeEnumVO vo) {
        ReminderTypeEnum entity =  ReminderTypeEnum.builder()
                    .reminderTypeEnumId(vo.getReminderTypeEnumId())
                    .typeName(vo.getTypeName())
                .build();
        boolean flag = this.reminderTypeEnumService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("reminderTypeEnum").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer reminderTypeEnumId) {
        ReminderTypeEnum entity = this.reminderTypeEnumService.getById(reminderTypeEnumId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer reminderTypeEnumId) {
        boolean flag = this.reminderTypeEnumService.removeById(reminderTypeEnumId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("reminderTypeEnum").data( ReminderTypeEnum.builder().reminderTypeEnumId(reminderTypeEnumId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ReminderTypeEnumPageVO pageVO) {

        LambdaQueryWrapper<ReminderTypeEnum> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getTypeName())) {
                                    queryWrapper.like(ReminderTypeEnum::getTypeName, pageVO.getTypeName());
                        }
            List<ReminderTypeEnum> list = reminderTypeEnumService.list(queryWrapper);
            ExcelUtil<ReminderTypeEnum> util = new ExcelUtil<>(ReminderTypeEnum.class);
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
            excelProvider.pdfData(file, ReminderTypeEnum.class, reminderTypeEnumService::saveBatch);
        }
        else{
            excelProvider.importData(file, ReminderTypeEnum.class, reminderTypeEnumService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(ReminderTypeEnum.class);
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
