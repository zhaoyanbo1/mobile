package com.kuafu.web.service;

import com.kuafu.web.dto.ImDtos.PageResult;
import com.kuafu.web.dto.ImDtos.SendMsgReq;
import com.kuafu.web.dto.ImDtos.MessageResp;

public interface ImService {
    Long ensureDmConversation(Long me, Long peerId);
    MessageResp send(Long senderId, SendMsgReq req);
    PageResult<MessageResp> history(Long me, Long conversationId, Long beforeId, int limit);
    void markRead(Long me, Long conversationId, Long lastReadMsgId);
    Long getPeerIdOfConversation(Long conversationId, Long selfId);
}
