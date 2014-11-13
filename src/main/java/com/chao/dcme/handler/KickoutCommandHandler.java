package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.Peer;

import javax.swing.*;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-13 10:59.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class KickoutCommandHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        String target = new String((byte[]) map.get("Content"));
        if (target.equals(LocalInfo.getLocalIdentifier())) {
            JOptionPane.showMessageDialog(null, "You have been kicked out!");
            frame.close();
        } else {
            Map<String, Peer> peers = LocalInfo.getPeers();
            peers.remove(target);
            JOptionPane.showMessageDialog(null, target + " has been kicked out");
        }
    }
}
