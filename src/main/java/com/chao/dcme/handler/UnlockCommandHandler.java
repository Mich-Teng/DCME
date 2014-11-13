package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;

import javax.swing.*;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-13 10:54.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class UnlockCommandHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        String origin = (String) map.get("Origin");
        if (LocalInfo.getLocker() != null && !origin.equals(LocalInfo.getLocker()))
            return;
        frame.setWritable();
        frame.appendLog("Unlocked by " + map.get("Origin"));
        JOptionPane.showMessageDialog(null, "You have been unlocked!");
        LocalInfo.setLocker(null);
    }
}
