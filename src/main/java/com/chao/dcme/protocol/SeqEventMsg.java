package com.chao.dcme.protocol;

import java.io.Serializable;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-06 09:53.
 * Package: com.chao.dcme.protocol
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class SeqEventMsg extends EventMsg implements Serializable {
    private int seqNo = 0;

    public SeqEventMsg(String origin, int eventType, byte[] content, int seqNo) {
        super(origin, eventType, content);
        this.seqNo = seqNo;
    }

    public SeqEventMsg(int seqNo) {
        this.seqNo = seqNo;
    }

}
