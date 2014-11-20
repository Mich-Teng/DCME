package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.LocalSender;
import com.chao.dcme.local.Peer;
import com.chao.dcme.ot_char.OT;
import com.chao.dcme.ot_char.Op;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.util.Utilities;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-09 17:18.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class InvitationMsgHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        frame.appendLog("Receiving invitation from " + map.get("Origin"));
        int choice = JOptionPane.showConfirmDialog(null, "Confirm Invitation?");
        Map<String, Object> reply = new HashMap<String, Object>();
        Integer event = (Integer) map.get("Event");
        reply.put("Event", event);
        reply.put("IP", LocalInfo.getLocalIp());
        reply.put("Port", LocalInfo.getLocalPort());
        if (choice == JOptionPane.YES_OPTION) {
            reply.put("Reply", true);
            // confirm the invitation
            byte[] content = (byte[]) map.get("Content");
            Object[] objects = (Object[]) Utilities.deserialize(content);
            Map<String, Peer> peers = (Map<String, Peer>) objects[0];
            LocalInfo.mergePeers(peers);
            // object[2] is initial str
            OT.init(LocalInfo.getPeers().size(), (String) objects[2]);
            OT.sethBuffer((List<Op>) objects[1]);
            OT.setPendingBuffer((List<Op>) objects[3]);
            for (Op tmp : (List<Op>) objects[1])
                OT.apply(tmp);
            frame.setText(OT.getText());
        } else {
            reply.put("Reply", false);
        }
        if (event == Event.INVITATION_RO) {
            LocalInfo.setIsRO(true);
            frame.setReadOnly();
        } else {
            frame.setWritable();
        }
        LocalSender.sendConfirmMsg(Utilities.serialize(reply));
    }
}
