package com.chao.dcme.local;

import com.chao.dcme.MainFrame;
import com.chao.dcme.handler.ChatMsgHandler;
import com.chao.dcme.handler.ConfirmMsgHandler;
import com.chao.dcme.handler.Handler;
import com.chao.dcme.handler.InvitationMsgHandler;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.util.Utilities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-04 20:50.
 * Package: com.chao.dcme.local
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LocalListener implements Runnable {
    private MainFrame mainFrame = null;
    private byte[] buffer = new byte[4096];

    public LocalListener(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        DatagramSocket socket = LocalInfo.getSocket();
        Handler[] handlers = new Handler[Event.EVENT_NUM];
        assignHandlers(handlers);
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                Map<String, Object> map = (Map<String, Object>) Utilities.deserialize(packet.getData());
                int type = (Integer) map.get("Event");
                handlers[type].execute(map, mainFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void assignHandlers(Handler[] handlers) {
        handlers[Event.CHAT_MESSAGE] = new ChatMsgHandler();
        handlers[Event.INVITATION] = new InvitationMsgHandler();
        handlers[Event.CONFIRM] = new ConfirmMsgHandler();
    }
}
