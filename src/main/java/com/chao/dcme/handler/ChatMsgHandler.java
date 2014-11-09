package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;

import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-09 16:52.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ChatMsgHandler implements Handler {
    @Override
    public void execute(Map<String, Object> map, MainFrame frame) {
        byte[] chatByte = (byte[]) map.get("Content");
        String chatStr = new String(chatByte);
        frame.appendChatMsg(map.get("Origin") + ": " + chatStr);
    }
}
