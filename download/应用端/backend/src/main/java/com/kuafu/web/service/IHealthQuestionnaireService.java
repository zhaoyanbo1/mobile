package com.kuafu.web.service;

import java.util.List;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>  健康问卷 服务类 </p>
 */
public interface IHealthQuestionnaireService extends IService<HealthQuestionnaire> {
    boolean saveOrUpdateWithScore(HealthQuestionnaire entity);
}
