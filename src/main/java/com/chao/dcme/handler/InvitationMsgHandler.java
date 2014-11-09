package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.LocalSender;
import com.chao.dcme.local.Peer;
import com.chao.dcme.util.Utilities;

import javax.swing.*;
import java.util.HashMap;
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
        reply.put("Event", map.get("Event"));
        reply.put("IP", LocalInfo.getLocalIp());
        reply.put("Port", LocalInfo.getLocalPort());
        if (choice == JOptionPane.YES_OPTION) {
            reply.put("Reply", true);
            // confirm the invitation
            byte[] content = (byte[]) map.get("Content");
            Map<String, Peer> peers = (Map<String, Peer>) Utilities.deserialize(content);
            LocalInfo.mergePeers(peers);
        } else {
            reply.put("Reply", false);
        }
        System.out.println("Send out Confirm info");
        LocalSender.sendConfirmMsg(Utilities.serialize(reply));
    }
}
