package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.LocalSender;
import com.chao.dcme.local.Peer;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.util.Utilities;
import com.chao.dcme.util.VoteTool;

import java.util.Map;
import java.util.Set;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-09 17:51.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ConfirmMsgHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        System.out.println("Receive Confirm Msg" + map);
        String origin = (String) map.get("Origin");
        Map<String, Object> reply = (Map<String, Object>) Utilities.deserialize((byte[]) map.get("Content"));
        Boolean isConfirmed = (Boolean) reply.get("Reply");
        Integer event = (Integer) reply.get("Event");
        switch (event) {
            case Event.INVITATION:
            case Event.INVITATION_RO:
                Set<String> invitee = LocalInfo.getInviteeCandidate();
                String ip = (String) reply.get("IP");
                Integer port = (Integer) reply.get("Port");
                if (invitee.contains(ip + ":" + port) && isConfirmed) {
                    invitee.remove(ip + ":" + port);
                    LocalInfo.addPeer(origin, new Peer(ip, port));
                }
                break;
            case Event.KICKOUT_REQEUST:
                String target = (String) reply.get("Target");
                VoteTool.vote(origin);
                if (VoteTool.isComplete()) {
                    LocalSender.sendKickoutCommandMsg(target.getBytes());
                }
                break;
            case Event.LOCK_REQUEST:
                VoteTool.vote(origin);
                if (VoteTool.isComplete()) {
                    LocalSender.sendLockCommandMsg();
                }
                break;
        }
    }
}
