package com.kuafu.web.controller;

import com.kuafu.web.dto.HealthPointDTO;
import com.kuafu.web.service.CareService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/care")
public class CareController {

    private final CareService careService;

    @GetMapping("/weekly")
    public R<List<HealthPointDTO>> weekly(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "limit", defaultValue = "7") Integer limit
    ) {
        List<HealthPointDTO> data = careService.weeklyPoints(userId, limit);
        return R.ok(data);
    }

    @GetMapping("/risk")
    public Map<String, Object> risk(@RequestParam("userId") Long userId) {
        String risk = careService.latestRiskLevel(userId);
        return Map.of("risk_level", risk);
    }

    // 简易 R；如果你已有通用 R，用你自己的即可
    @Data
    static class R<T> {
        private int code;
        private String message;
        private boolean success;
        private T data;
        static <T> R<T> ok(T data) {
            R<T> r = new R<>();
            r.code = 0; r.success = true; r.data = data;
            r.message = "OK";
            return r;
        }
    }

}
