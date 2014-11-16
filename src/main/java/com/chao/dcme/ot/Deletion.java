package com.chao.dcme.ot;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 15:16.
 * Package: com.chao.dcme.ot
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Deletion {
    // the length of deleted str
    int dlen = 0;
    // start pos of the deletion
    int pos = 0;

    public Deletion(int dlen, int pos) {
        this.dlen = dlen;
        this.pos = pos;
    }

    public int getDlen() {
        return dlen;
    }

    public void setDlen(int dlen) {
        this.dlen = dlen;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
