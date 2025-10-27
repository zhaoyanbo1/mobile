package com.kuafu.web.service.impl;

import com.kuafu.web.dto.ImDtos.MessageResp;
import com.kuafu.web.dto.ImDtos.PageResult;
import com.kuafu.web.dto.ImDtos.SendMsgReq;
import com.kuafu.web.mapper.ImConversationMapper;
import com.kuafu.web.mapper.ImConversationMemberMapper;
import com.kuafu.web.mapper.ImDmPairMapper;
import com.kuafu.web.mapper.ImMessageMapper;
import com.kuafu.web.model.ImConversation;
import com.kuafu.web.model.ImConversationMember;
import com.kuafu.web.model.ImDmPair;
import com.kuafu.web.model.ImMessage;
import com.kuafu.web.service.ImService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImServiceImpl implements ImService {

    private final ImConversationMapper conversationMapper;
    private final ImConversationMemberMapper memberMapper;
    private final ImDmPairMapper dmPairMapper;
    private final ImMessageMapper messageMapper;

    @Override
    @Transactional
    public Long ensureDmConversation(Long me, Long peerId) {
        if (Objects.equals(me, peerId)) throw new IllegalArgumentException("不能与自己创建会话");
        long a = Math.min(me, peerId), b = Math.max(me, peerId);
        ImDmPair exist = dmPairMapper.selectByPair(a, b);
        if (exist != null) return exist.getConversationId();

        ImConversation conv = new ImConversation();
        conv.setType("DM");
        conv.setCreatedBy(me);
        conversationMapper.insert(conv);
        Long convId = conv.getConversationId();

        memberMapper.insert(new ImConversationMember(null, convId, me, null, 0, null));
        memberMapper.insert(new ImConversationMember(null, convId, peerId, null, 0, null));

        dmPairMapper.insert(new ImDmPair(null, a, b, convId, null));
        return convId;
    }

    @Override
    @Transactional
    public MessageResp send(Long senderId, SendMsgReq req) {
        Long convId = req.getConversationId();
        if (convId == null) {
            if (req.getPeerId() == null) throw new IllegalArgumentException("缺少 conversationId 或 peerId");
            convId = ensureDmConversation(senderId, req.getPeerId());
        }
        if (memberMapper.countMember(convId, senderId) == 0) {
            throw new IllegalStateException("无权发送（非会话成员）");
        }

        ImMessage msg = new ImMessage();
        msg.setConversationId(convId);
        msg.setSenderId(senderId);
        msg.setContentType(req.getContentType() != null ? req.getContentType() : "TEXT");
        msg.setContent(req.getContent());
        msg.setStatus("SENT");
        messageMapper.insert(msg);

        conversationMapper.updateLastMessage(convId, msg.getMessageId());

        return toResp(msg);
    }

    @Override
    public PageResult<MessageResp> history(Long me, Long conversationId, Long beforeId, int limit) {
        if (memberMapper.countMember(conversationId, me) == 0) {
            throw new IllegalStateException("无权查看历史（非会话成员）");
        }
        List<ImMessage> list = messageMapper.pageByConversation(conversationId, beforeId, limit);
        PageResult<MessageResp> pr = new PageResult<>();
        pr.setList(list.stream().map(this::toResp).collect(Collectors.toList()));
        if (list.size() == limit) {
            pr.setNextBeforeId(list.get(list.size() - 1).getMessageId());
        } else {
            pr.setNextBeforeId(null);
        }
        return pr;
    }

    @Override
    @Transactional
    public void markRead(Long me, Long conversationId, Long lastReadMsgId) {
        if (memberMapper.countMember(conversationId, me) == 0) {
            throw new IllegalStateException("无权（非会话成员）");
        }
        memberMapper.updateLastRead(conversationId, me, lastReadMsgId);
    }

    @Override
    public Long getPeerIdOfConversation(Long conversationId, Long selfId) {
        return memberMapper.peerId(conversationId, selfId);
    }

    private MessageResp toResp(ImMessage m) {
        MessageResp r = new MessageResp();
        r.setMessageId(m.getMessageId());
        r.setConversationId(m.getConversationId());
        r.setSenderId(m.getSenderId());
        r.setContentType(m.getContentType());
        r.setContent(m.getContent());

        if (m.getCreatedAt() != null) {
            r.setCreatedAtEpochMs(
                    m.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            );
        } else {
            r.setCreatedAtEpochMs(System.currentTimeMillis());
        }

        return r;
    }
}
