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
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("HealthQuestionnaire")
public class HealthQuestionnaireServiceImpl extends ServiceImpl<HealthQuestionnaireMapper, HealthQuestionnaire> implements IHealthQuestionnaireService {


}
