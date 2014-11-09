package com.chao.dcme.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-06 09:36.
 * Package: com.chao.dcme.protocol
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class EventMsg implements Serializable {
    private String origin = "";
    private int eventType = 0;
    private byte[] content = null;

    public EventMsg(String origin, int eventType, byte[] content) {
        this.origin = origin;
        this.eventType = eventType;
        this.content = content;
    }

    public EventMsg() {
    }

    public Map<String, Object> packAsMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Origin", origin);
        map.put("Event", eventType);
        map.put("Content", content);
        return map;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
