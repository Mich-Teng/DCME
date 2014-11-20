package com.chao.dcme.ot_char;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-18 22:29.
 * Package: com.chao.dcme.ot_char
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Insertion extends Op {
    int pos;
    char c;

    public Insertion(StateVector stateVec, int pos, char c) {
        super(stateVec);
        this.pos = pos;
        this.c = c;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Insert " + c + " at pos " + pos;
    }

    @Override
    public int getOpType() {
        return OpType.INSERT_CHAR;
    }

    @Override
    public Deletion inverse() {
        return new Deletion(stateVec, pos);
    }
}
