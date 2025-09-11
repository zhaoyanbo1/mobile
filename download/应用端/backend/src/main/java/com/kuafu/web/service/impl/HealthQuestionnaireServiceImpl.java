package com.kuafu.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafu.web.mapper.HealthQuestionnaireMapper;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.service.IHealthQuestionnaireService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 健康问卷 服务实现类 </p>
 */
@Slf4j
@Service("HealthQuestionnaire")
@RequiredArgsConstructor
public class HealthQuestionnaireServiceImpl extends ServiceImpl<HealthQuestionnaireMapper, HealthQuestionnaire> implements IHealthQuestionnaireService {
    @Override
    public boolean saveOrUpdateWithScore(HealthQuestionnaire entity) {
        computeScore(entity);
        return this.saveOrUpdate(entity);
    }

    private void computeScore(HealthQuestionnaire entity) {
        int total = 0;
        total += value(entity.getAdl());
        total += value(entity.getMobilityOut());
        total += value(entity.getFalls());
        total += value(entity.getWeightLoss());
        int diseaseCount = 0;
        if (entity.getDiseases() != null && !entity.getDiseases().isEmpty()) {
            diseaseCount = entity.getDiseases().split(",").length;
        }
        total += Math.min(diseaseCount, 6);
        total += value(entity.getPaMinutes());
        total += value(entity.getPaWillingness());
        total += value(entity.getFluVaccine());
        total += value(entity.getPolypharmacy());
        total += value(entity.getSocial());
        total += value(entity.getFvServes());
        entity.setTotalScore(total);
        String risk;
        if (total <= 6) {
            risk = "低风险";
        } else if (total <= 14) {
            risk = "中等风险";
        } else if (total <= 22) {
            risk = "高风险";
        } else {
            risk = "极高风险";
        }
        entity.setRiskLevel(risk);
    }

    private int value(Integer v) {
        return v == null ? 0 : v;
    }

}
