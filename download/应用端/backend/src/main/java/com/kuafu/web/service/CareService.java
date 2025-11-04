package com.kuafu.web.service;

import com.kuafu.web.dto.HealthPointDTO;
import com.kuafu.web.mapper.HealthQuestionnaireMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CareService {

    private final HealthQuestionnaireMapper mapper;
    private static final SimpleDateFormat MD = new SimpleDateFormat("MM/dd");
    private final HealthQuestionnaireMapper healthQuestionnaireMapper;


    public List<HealthPointDTO> weeklyPoints(Long userId, int limit) {
        List<Map<String, Object>> rows = mapper.selectWeeklyByUser(userId, limit);
        List<HealthPointDTO> out = new ArrayList<>(rows.size());
        for (int i = rows.size() - 1; i >= 0; i--) { // 从旧到新
            Map<String, Object> r = rows.get(i);
            Date ct = castDate(r.get("creationTime"));
            Number score = (Number) r.getOrDefault("totalScore", 0);
            HealthPointDTO dto = new HealthPointDTO();
            long ts = (ct != null) ? ct.getTime() : 0L;
            dto.setTs(ts);
            dto.setDay(ts > 0 ? MD.format(ct) : "");
            dto.setScore(score == null ? 0 : score.intValue());
            out.add(dto);
        }
        return out;
    }

    public String latestRiskLevel(Long userId) {
        if (userId == null) return "low_risk";
        String rl = healthQuestionnaireMapper.selectLatestRiskLevel(userId);
        return (rl == null || rl.isBlank()) ? "low_risk" : rl.trim();
    }

    private Date castDate(Object o) {
        if (o instanceof Date) {
            return (Date) o;
        }
        return null;
    }
}
