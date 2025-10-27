package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.model.ImDmPair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImDmPairMapper extends BaseMapper<ImDmPair> {
    ImDmPair selectByPair(@Param("userA") Long userA,
                          @Param("userB") Long userB);
}
