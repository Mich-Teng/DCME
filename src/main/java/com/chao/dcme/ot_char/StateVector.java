package com.chao.dcme.ot_char;

import java.io.Serializable;
import java.util.Vector;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 19:54.
 * Package: com.chao.dcme.ot
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class StateVector implements Serializable {
    // state vector
    int[] vec = new int[15];
    // id of this State Vector
    int id;

    public StateVector(int id) {
        this.id = id;
    }

    public int get(int id) {
        return vec[id];
    }

    public void set(int id, int val) {
        vec[id] = val;
    }

    public void addOne(int id) {
        vec[id]++;
    }


    public int getId() {
        return id;
    }

    public int size() {
        return 15;
    }
}
