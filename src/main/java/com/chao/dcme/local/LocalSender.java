package com.chao.dcme.local;

import com.chao.dcme.exception.InvalidEventException;
import com.chao.dcme.ot_char.OT;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.protocol.EventMsg;
import com.chao.dcme.protocol.FloodProtocol;
import com.chao.dcme.protocol.SeqEventMsg;
import com.chao.dcme.util.Utilities;
import com.chao.dcme.util.VoteTool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public static void sendOTMsg(int pos) {
        OT.updateStateVec(OT.getId());
        EventMsg eventMsg = new EventMsg(LocalInfo.getLocalIdentifier(), Event.OT_DELETION,
                Utilities.serialize(pos));
        FloodProtocol.floodMsg(Utilities.serialize(eventMsg.packAsMap()));
    }

    public static void sendOTMsg(int pos, char c) {
        OT.updateStateVec(OT.getId());
        Object[] arr = new Object[2];
        arr[0] = pos;
        arr[1] = c;
        EventMsg eventMsg = new EventMsg(LocalInfo.getLocalIdentifier(), Event.OT_INSERTION,
                Utilities.serialize(arr));
        FloodProtocol.floodMsg(Utilities.serialize(eventMsg.packAsMap()));
    }
    public static void sendInvitationMsg(String ip, int port, final int type) throws InvalidEventException {
        // todo send current context to the user
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
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
    }

    public static void sendLockCommandMsg() {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.LOCK_COMMAND, new byte[1],
                LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
    }

    public static void sendUnlockMsg() {
        LocalInfo.updateSeqNo();
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.UNLOCK,
                new byte[1], LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));

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
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
    }

    public static void sendKickoutRequestMsg(String target) {
        if (target == null || target.equals(""))
            return;
        LocalInfo.updateSeqNo();
        Set<String> set = new HashSet<String>(LocalInfo.getPeers().keySet());
        set.remove(target);
        VoteTool.refresh(set);
        SeqEventMsg msg = new SeqEventMsg(LocalInfo.getLocalIdentifier(), Event.KICKOUT_REQEUST,
                target.getBytes(), LocalInfo.getSeqNo());
        FloodProtocol.floodMsg(Utilities.serialize(msg.packAsMap()));
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
