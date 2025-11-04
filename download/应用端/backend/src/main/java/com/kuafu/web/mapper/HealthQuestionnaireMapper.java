package com.kuafu.web.mapper;

import java.util.List;
import java.util.Map;

import com.kuafu.web.entity.HealthQuestionnaire;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p> 健康问卷 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface HealthQuestionnaireMapper extends BaseMapper<HealthQuestionnaire> {
    /**
     * 最近 limit 条（按 creation_time 倒序），只查两列
     * 说明：这里直接返回 Map，省去写实体；XML 里用 AS 起别名。
     */
    List<Map<String, Object>> selectWeeklyByUser(
            @Param("userId") Long userId,
            @Param("limit") Integer limit
    );

    String selectLatestRiskLevel(@Param("userId") Long userId);

}
