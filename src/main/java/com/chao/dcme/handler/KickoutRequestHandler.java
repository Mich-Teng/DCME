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
 * Date: 2014-11-13 10:29.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class KickoutRequestHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        frame.appendLog("Receiving kickout request from " + map.get("Origin"));
        String target = new String((byte[]) map.get("Content"));
        // if target is myself, don't vote, just return
        if (target.equals(LocalInfo.getLocalIdentifier()))
            return;
        int choice = JOptionPane.showConfirmDialog(null, "Confirm Kickout " + target + "?");
        Map<String, Object> reply = new HashMap<String, Object>();
        Integer event = (Integer) map.get("Event");
        reply.put("Event", event);
        reply.put("Target", target);
        if (choice == JOptionPane.YES_OPTION) {
            reply.put("Reply", true);
            // confirm the invitation
            byte[] content = (byte[]) map.get("Content");
            Map<String, Peer> peers = (Map<String, Peer>) Utilities.deserialize(content);
            LocalInfo.mergePeers(peers);
        } else {
            reply.put("Reply", false);
        }
        LocalSender.sendConfirmMsg(Utilities.serialize(reply));
    }
}
