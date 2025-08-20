package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.SystemSettingsAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 系统设置 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Mapper
public interface SystemSettingsAllMapper extends BaseMapper<SystemSettingsAll> {
    public List<SystemSettingsAll> selectListNew(@Param("ew") Wrapper<SystemSettingsAll> queryWrapper);
}
