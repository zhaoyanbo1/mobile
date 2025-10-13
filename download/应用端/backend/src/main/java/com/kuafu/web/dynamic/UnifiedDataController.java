package com.kuafu.web.dynamic;


import com.kuafu.common.domin.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * 统一入口
 */
@RestController
@RequestMapping("/api/data")
public class UnifiedDataController {

    @Autowired
    private DynamicServiceInvoker dynamicServiceInvoker;

    /**
     * 统一入口
     *
     * @param table
     * @param method page/list/add/update/get/delete
     * @param data
     * @return
     */
    @PostMapping("/invoke")
    public Object handle(
            @RequestParam("table") String table,
            @RequestParam(value = "method", defaultValue = "save") String method,
            @RequestBody Map<String, Object> data
    ) {
        try {

            if (StringUtils.equalsAnyIgnoreCase(method, "get", "list", "page")) {
                boolean check = VoConverter.checkTableAllExist(table);
                if (check) {
                    table = table + "_all";
                }
            }

            Object vo;
            if (StringUtils.equalsIgnoreCase(method, "page")) {
                vo = VoConverter.convertPageVo(table, data);
            } else if (StringUtils.endsWith(table, "_all")) {
                vo = VoConverter.convertPageVo(table, data);
            } else {
                vo = VoConverter.convert(table, data);
            }
            // 手动覆盖 reminderTime
            if (data.containsKey("reminder_time")) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime local = LocalDateTime.parse(data.get("reminder_time").toString(), fmt);
                LocalDateTime fixed = local.plusHours(2).plusMinutes(30);
//                ZoneId adelaide = ZoneId.of("Australia/Adelaide");
                ZonedDateTime zoned = fixed.atZone(ZoneId.systemDefault());
                Date date = Date.from(zoned.toInstant());
                try {
                    var field = vo.getClass().getDeclaredField("reminderTime");
                    field.setAccessible(true);
                    field.set(vo, date);
                    System.out.println("[VO Overwrite] reminderTime = " + date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            System.out.println("vo type = " + vo.getClass().getName());
//            var getter = vo.getClass().getMethod("getReminderTime");
//            Object value = getter.invoke(vo);
//            System.out.println("reminderTime = " + value);
            return dynamicServiceInvoker.invoke(table, vo, method);
        } catch (Exception e) {

            return ResultUtils.error(e.getMessage());
        }
    }
}
