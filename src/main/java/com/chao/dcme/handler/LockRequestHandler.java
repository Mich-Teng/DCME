package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.LocalSender;
import com.chao.dcme.util.Utilities;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-13 10:42.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LockRequestHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        String origin = (String) map.get("Origin");
        frame.appendLog("Receiving lock request from " + origin);
        int choice = JOptionPane.showConfirmDialog(null, "Confirm Lock Request from " + origin + "?");
        Map<String, Object> reply = new HashMap<String, Object>();
        Integer event = (Integer) map.get("Event");
        reply.put("Event", event);
        if (choice == JOptionPane.YES_OPTION) {
            reply.put("Reply", true);
            LocalInfo.setLocker(origin);
        } else {
            reply.put("Reply", false);
        }
        LocalSender.sendConfirmMsg(Utilities.serialize(reply));
    }
}
