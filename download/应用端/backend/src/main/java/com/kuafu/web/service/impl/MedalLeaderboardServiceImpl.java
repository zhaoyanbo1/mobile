package com.kuafu.web.service.impl;

import com.kuafu.web.mapper.MedalLeaderboardMapper;
import com.kuafu.web.service.FriendService;
import com.kuafu.web.service.MedalLeaderboardService;
import com.kuafu.web.vo.MedalLeaderboardVO;
import com.kuafu.web.vo.MyMedalStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//@Service
//public class MedalLeaderboardServiceImpl implements MedalLeaderboardService {
//
//    private final MedalLeaderboardMapper mapper;
//    private final FriendService friendService;
//
//    public MedalLeaderboardServiceImpl(MedalLeaderboardMapper mapper) {
//        this.mapper = mapper;
//    }
//
//    @Override
//    public List<MedalLeaderboardVO> page(int page, int size) {
//        int offset = Math.max(page, 1);
//        offset = (offset - 1) * size;
//        return mapper.page(offset, size);   // MySQL 版的 mapper.page
//    }
//
//    @Override
//    public long totalUsersOnBoard() {
//        return mapper.countLeaderboardUsers();
//    }
//
//    @Override
//    public MyMedalStatVO myStat(Long userId) {
//        return mapper.selectMyMedalStat(userId);
//    }
//}
@Service
@RequiredArgsConstructor
public class MedalLeaderboardServiceImpl implements MedalLeaderboardService {

    private final MedalLeaderboardMapper leaderboardMapper;
    private final FriendService friendService;

    @Override
    public long countLeaderboardUsers() {
        return leaderboardMapper.countLeaderboardUsers();
    }

    @Override
    public List<MedalLeaderboardVO> pageWithFriendStatus(Long currentUserId, int pageNo, int pageSize) {
        int limit = Math.max(1, pageSize);
        int offset = Math.max(0, (pageNo - 1) * pageSize);



        List<MedalLeaderboardVO> rows = leaderboardMapper.page(offset, limit);
        if (rows.isEmpty()) return rows;

        if (currentUserId == null) {
            rows.forEach(vo -> { vo.setMe(false); vo.setFriendStatus("NONE"); vo.setFriendRequestId(null); });
            return rows;
        }

        Set<Long> userIds = rows.stream().map(MedalLeaderboardVO::getUserId).collect(Collectors.toSet());
        Map<Long, String> statusMap = friendService.batchStatuses(currentUserId, userIds);
        Map<Long, Long> reqIdMap = friendService.batchPendingRequestIds(currentUserId, userIds);

        for (MedalLeaderboardVO vo : rows) {
            boolean isMe = currentUserId.equals(vo.getUserId());
            vo.setMe(isMe);
            if (isMe) { vo.setFriendStatus("FRIEND"); vo.setFriendRequestId(null); }
            else {
                vo.setFriendStatus(statusMap.getOrDefault(vo.getUserId(), "NONE"));
                vo.setFriendRequestId(reqIdMap.get(vo.getUserId()));
            }
        }
        return rows;
    }

    @Override
    public MyMedalStatVO myMedalStat(Long currentUserId) {
        if (currentUserId == null) return null;
        return leaderboardMapper.selectMyMedalStat(currentUserId);
    }
}
