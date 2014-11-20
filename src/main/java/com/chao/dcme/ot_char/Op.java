package com.chao.dcme.ot_char;


/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 17:44.
 * Package: com.chao.dcme.ot
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Op {
    StateVector stateVec = null;


    public int getOpType() {
        return OpType.UNKNOWN;
    }

    public Op(StateVector stateVec) {
        this.stateVec = stateVec;
    }

    public StateVector getStateVec() {
        return stateVec;
    }

    public void setStateVec(StateVector stateVec) {
        this.stateVec = stateVec;
    }

    public Op inverse() {
        return null;
    }

    ;
}
