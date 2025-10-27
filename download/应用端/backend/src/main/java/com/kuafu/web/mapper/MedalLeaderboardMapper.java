package com.kuafu.web.mapper;

import com.kuafu.web.vo.MedalLeaderboardVO;
import com.kuafu.web.vo.MyMedalStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MedalLeaderboardMapper {
    long countLeaderboardUsers();                                     // 上榜用户数（≥1枚奖牌）
    List<MedalLeaderboardVO> page(@Param("offset") int offset,
                                  @Param("limit") int limit);         // 分页列表
//    MyMedalStatVO myStat(@Param("userId") Long userId);               // 我的统计 + 排名
    MyMedalStatVO selectMyMedalStat(@Param("userId") Long userId);
}
