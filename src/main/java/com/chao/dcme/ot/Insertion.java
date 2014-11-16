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

public class Insertion {
    String str = null;
    int pos = 0;

    public Insertion(String str, int pos) {
        this.str = str;
        this.pos = pos;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
