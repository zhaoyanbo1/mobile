package com.kuafu.web.service;

import com.kuafu.web.vo.MedalLeaderboardVO;
import com.kuafu.web.vo.MyMedalStatVO;
import java.util.List;

//public interface MedalLeaderboardService {
//    List<MedalLeaderboardVO> page(Long currentUserId, int page, int size);
//    long totalUsersOnBoard();
//    MyMedalStatVO myStat(Long userId);
//
//
//}
public interface MedalLeaderboardService {
    long countLeaderboardUsers();
    List<MedalLeaderboardVO> pageWithFriendStatus(Long currentUserId, int pageNo, int pageSize);
    MyMedalStatVO myMedalStat(Long currentUserId);
}