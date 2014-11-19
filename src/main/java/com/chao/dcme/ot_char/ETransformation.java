package com.chao.dcme.ot_char;

import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-18 22:45.
 * Package: com.chao.dcme.ot_char
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ETransformation {
    public static Insertion ii(Insertion i1, Insertion i2) {
        StateVector vec = i1.getStateVec();
        int p1 = i1.getPos(), p2 = i2.getPos();
        char c1 = i1.getC(), c2 = i2.getC();
        if (p1 <= p2)
            return i1;
        return new Insertion(vec, p1 - 1, c1);
    }

    public static Insertion id(Insertion i1, Deletion d1) {
        StateVector vec = i1.getStateVec();
        int p1 = i1.getPos(), p2 = d1.getPos();
        char c1 = i1.getC();
        if (p1 <= p2)
            return i1;
        return new Insertion(vec, p1 + 1, c1);
    }

    public static Deletion di(Deletion d1, Insertion i1) {
        StateVector vec = d1.getStateVec();
        int p1 = d1.getPos(), p2 = i1.getPos();
        if (p1 < p2)
            return d1;
        return new Deletion(vec, p1 - 1);
    }

    public static Deletion dd(Deletion d1, Deletion d2) {
        StateVector vec = d1.getStateVec();
        int p1 = d1.getPos(), p2 = d2.getPos();
        if (p1 <= p2)
            return d1;
        else
            return new Deletion(vec, p1 + 1);
    }

    public static Op ET(Op a, Op b) {
        if (a == null)
            return null;
        // null means identity operation here
        if (b == null)
            return a;
        int at = a.getOpType(), bt = b.getOpType();
        if (at == OpType.INSERT_CHAR && bt == OpType.INSERT_CHAR)
            return ii((Insertion) a, (Insertion) b);
        if (at == OpType.DELETE_CHAR && bt == OpType.DELETE_CHAR)
            return dd((Deletion) a, (Deletion) b);
        if (at == OpType.INSERT_CHAR && bt == OpType.DELETE_CHAR)
            return id((Insertion) a, (Deletion) b);
        if (at == OpType.DELETE_CHAR && bt == OpType.INSERT_CHAR)
            return di((Deletion) a, (Insertion) b);
        return null;
    }

    public static Op LET(Op op, List<Op> ops) {
        if (ops == null || ops.isEmpty())
            return op;
        Op tmp = op;
        for (int i = 0; i < ops.size(); i++) {
            tmp = ET(tmp, ops.get(i));
        }
        return tmp;
    }
}
