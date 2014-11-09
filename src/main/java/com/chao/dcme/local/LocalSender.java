package com.chao.dcme.local;

import com.chao.dcme.exception.InvalidEventException;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.protocol.EventMsg;
import com.chao.dcme.protocol.FloodProtocol;
import com.chao.dcme.protocol.SeqEventMsg;
import com.chao.dcme.util.Utilities;
import com.chao.dcme.util.VoteTool;

import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-04 21:13.
 * Package: com.chao.dcme.local
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LocalSender {
    public static void sendInvitationMsg(String ip, int port, final int type) throws InvalidEventException {
        if (type != Event.INVITATION && type != Event.INVITATION_RO)
            throw new InvalidEventException("Invalid event type: " + type);
        // get the content of serialized table info
        Map<String, Peer> map = new HashMap<String, Peer>(LocalInfo.getPeers());
        map.put(LocalInfo.getLocalIdentifier(), new Peer(LocalInfo.getLocalIp(), LocalInfo.getLocalPort()));
        byte[] content = Utilities.serialize(map);
        EventMsg msg = new EventMsg(LocalInfo.getLocalIdentifier(), type, content);
        // add it into candidate invitee
        LocalInfo.addInviteeCandidate(ip, port);
        // send it to the target
        Utilities.send(Utilities.serialize(msg.packAsMap()), ip, port);
    }

    public static void sendLockRequestMsg() {
        LocalInfo.updateSeqNo();
        VoteTool.refresh(LocalInfo.getPeers().keySet());
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.LOCK_REQUEST, new byte[1],
                LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg));
    }

    /**
     * send msg point to point
     */
    public static void sendLockCommandMsg() {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.LOCK_COMMAND, new byte[1],
                LocalInfo.getSeqNo());
    }

    public static void sendUnlockMsg() {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.LOCK_REQUEST_RETRIEVAL,
                new byte[1], LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg));

    }

    /**
     * flood kick out command to let other peers know target is kicked out
     *
     * @param target target identifier
     */
    public static void sendKickoutCommandMsg(byte[] target) {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.KICKOUT_COMMAND,
                target, LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg));
    }

    public static void sendKickoutRequestMsg(String target) {
        if (target == null || target.equals(""))
            return;
        LocalInfo.updateSeqNo();
        VoteTool.refresh(LocalInfo.getPeers().keySet());
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.KICKOUT_REQEUST,
                target.getBytes(), LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg));
    }

    public static void sendChatMsg(String text) {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.CHAT_MESSAGE, text.getBytes(),
                LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
    }

    public static void sendConfirmMsg(byte[] content) {
        EventMsg msg = new EventMsg(LocalInfo.getLocalIdentifier(), Event.CONFIRM, content);
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
    }

}
