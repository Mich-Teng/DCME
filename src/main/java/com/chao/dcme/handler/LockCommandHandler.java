package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.local.LocalInfo;

import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-13 10:50.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LockCommandHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        String origin = (String) map.get("Origin");
        if (!origin.equals(LocalInfo.getLocker()))
            return;
        frame.appendLog("Locked by " + map.get("Origin"));
        frame.setReadOnly();
    }
}
