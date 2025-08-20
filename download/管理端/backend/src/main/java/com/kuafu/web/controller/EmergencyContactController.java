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
import com.kuafu.web.entity.EmergencyContact;
import com.kuafu.web.service.IEmergencyContactService;
import com.kuafu.web.vo.EmergencyContactPageVO;
import com.kuafu.web.vo.EmergencyContactVO;
import com.kuafu.web.event.MyEventService;
import com.kuafu.web.event.EventVo;
/**
 * <p> 紧急联系人 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/emergencyContact")
@Api(tags = {"紧急联系人"})
public class EmergencyContactController  {

    private final IEmergencyContactService emergencyContactService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody EmergencyContactPageVO pageVO){
        IPage<EmergencyContact> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getName())) {
            queryWrapper.like(EmergencyContact::getName, pageVO.getName());
        }
            if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(EmergencyContact::getPhoneNumber, pageVO.getPhoneNumber());
        }
        return ResultUtils.success(emergencyContactService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody EmergencyContactVO vo){
        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like(EmergencyContact::getName, vo.getName());
        }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(EmergencyContact::getPhoneNumber, vo.getPhoneNumber());
        }
        return ResultUtils.success(emergencyContactService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(EmergencyContact::getEmergencyContactId);

        List<EmergencyContact> list =emergencyContactService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getEmergencyContactId(), l.getEmergencyContactId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody EmergencyContactVO vo) {
        EmergencyContact entity =  EmergencyContact.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .phoneNumber(vo.getPhoneNumber())
                .build();
        boolean flag =this.emergencyContactService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("emergencyContact").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody EmergencyContactVO vo) {
        EmergencyContact entity =  EmergencyContact.builder()
                    .emergencyContactId(vo.getEmergencyContactId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .name(vo.getName())
                    .phoneNumber(vo.getPhoneNumber())
                .build();
        boolean flag = this.emergencyContactService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("emergencyContact").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer emergencyContactId) {
        EmergencyContact entity = this.emergencyContactService.getById(emergencyContactId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer emergencyContactId) {
        boolean flag = this.emergencyContactService.removeById(emergencyContactId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("emergencyContact").data( EmergencyContact.builder().emergencyContactId(emergencyContactId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody EmergencyContactPageVO pageVO) {

        LambdaQueryWrapper<EmergencyContact> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getUserInfoUserInfoId1() != null){
                            queryWrapper.eq(EmergencyContact::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getName())) {
                                    queryWrapper.like(EmergencyContact::getName, pageVO.getName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                            queryWrapper.eq(EmergencyContact::getPhoneNumber, pageVO.getPhoneNumber());
                        }
            List<EmergencyContact> list = emergencyContactService.list(queryWrapper);
            ExcelUtil<EmergencyContact> util = new ExcelUtil<>(EmergencyContact.class);
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
            excelProvider.pdfData(file, EmergencyContact.class, emergencyContactService::saveBatch);
        }
        else{
            excelProvider.importData(file, EmergencyContact.class, emergencyContactService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(EmergencyContact.class);
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
