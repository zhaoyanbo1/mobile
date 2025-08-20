package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.HealthQuestionnaireAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  健康问卷 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
public interface IHealthQuestionnaireAllService extends IService<HealthQuestionnaireAll> {
        List<HealthQuestionnaireAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper);
        List<HealthQuestionnaireAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper);

        IPage pageNew(IPage<HealthQuestionnaireAll> page, PageRequest pageRequest, LambdaQueryWrapper<HealthQuestionnaireAll> queryWrapper);
        }
