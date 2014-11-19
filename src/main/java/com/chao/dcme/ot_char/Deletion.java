package com.chao.dcme.ot_char;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-18 22:31.
 * Package: com.chao.dcme.ot_char
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Deletion extends Op {
    int pos;

    public Deletion(StateVector stateVec, int pos) {
        super(stateVec);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public int getOpType() {
        return OpType.DELETE_CHAR;
    }

    @Override
    public String toString() {
        return "Delete at pos " + pos;
    }
}
