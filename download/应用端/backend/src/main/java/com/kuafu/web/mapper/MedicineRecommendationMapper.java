package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.MedicineRecommendation;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p> 药品推荐 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface MedicineRecommendationMapper extends BaseMapper<MedicineRecommendation> {

}
