package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;
import com.chao.dcme.ot_char.*;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.util.Utilities;

import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-19 00:02.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class OTHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        frame.appendLog("OT Operation from " + map.get("Origin"));
        Integer event = (Integer) map.get("Event");
        Op op = null;
        Object[] arr = (Object[]) Utilities.deserialize((byte[]) map.get("Content"));
        if (event == Event.OT_INSERTION) {
            op = new Insertion((StateVector) arr[0], (Integer) arr[1], (Character) arr[2]);
        } else {
            op = new Deletion((StateVector) arr[0], (Integer) arr[1]);
        }
        OT.transform(op);
    }
}
