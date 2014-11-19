package com.chao.dcme.ot_char;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 17:49.
 * Package: com.chao.dcme.ot
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class OT {
    // historical buffer
    private static List<Op> hBuffer = new ArrayList<Op>();
    // identifier of this node
    private static int id = 0;
    // state vector of this node
    private static StateVector stateVector = null;


    public static void init(int idNum) {
        id = idNum;
        stateVector = new StateVector(id);
    }

    // whether Oa --> Ob
    private static boolean isDependent(StateVector a, StateVector b, int i) {
        // if b[id] >= a[id], it means that before b is generated, b has the
        // knowledge of a[id], so b is dependent on a
        return a.get(i) <= b.get(i);
    }

    // Give a new causally ready operation Onew, and HB, return the excution form
    // of Onew. Intention-preserving scheme
    private static Op GOT(Op newOp) {
        int k = 0, j = 0;
        StateVector vecNew = newOp.getStateVec();
        for (k = 0; k < hBuffer.size(); k++) {
            Op opTmp = hBuffer.get(k);
            StateVector vecTmp = opTmp.getStateVec();
            // a, b are independent
            if (!isDependent(vecTmp, vecNew, vecTmp.getId()) && !isDependent(vecNew, vecTmp, vecNew.getId()))
                break;
        }
        if (k == hBuffer.size()) {
            // no independent operation in the buffer
            return newOp;
        }
        // list of operations in HB[k+1...m] which are causally preceding Onew
        List<Op> eol = new ArrayList<Op>();
        // index of these operations
        List<Integer> c = new ArrayList<Integer>();
        for (int i = k + 1; i < hBuffer.size(); i++) {
            Op opTmp = hBuffer.get(i);
            StateVector vecTmp = opTmp.getStateVec();
            if (isDependent(vecTmp, vecNew, vecTmp.getId())) {
                eol.add(opTmp);
                c.add(i);
            }
        }
        if (eol.isEmpty()) {
            // all the operations between [k+1...m] are also independent with Onew
            return LIT(newOp, hBuffer.subList(k, hBuffer.size() - 1));
        }
        List<Op> eolPrime = new ArrayList<Op>();
        for (int i = 0; i < eol.size(); i++) {
            int ci = c.get(i);
            List<Op> his = new ArrayList<Op>(hBuffer.subList(k, ci - 1));
            Collections.reverse(his);
            Op opTmp = LET(eol.get(i), his);
            eolPrime.add(LIT(opTmp, eolPrime));
        }
        Collections.reverse(eolPrime);
        Op onewPrime = LET(newOp, eolPrime);
        return LIT(onewPrime, hBuffer.subList(k, hBuffer.size() - 1));
    }

    public static void transform(Op opNew) {
        List<Op> redo = new ArrayList<Op>();
        StateVector vecNew = opNew.getStateVec();
        for (int i = hBuffer.size() - 1; i >= 0; i--) {
            Op op = hBuffer.get(i);
            StateVector vecTmp = op.getStateVec();
            if (!isDependent(vecTmp, vecNew, vecTmp.getId())) {
                redo.add(op);
            } else {
                break;
            }
        }
        int m = hBuffer.size() - 1 - redo.size();
        // remove the last $count operations

        Op eopNew = GOT(opNew);

        System.out.println(eopNew.toString());


        List<Op> eolPrime = new ArrayList<Op>();
        eolPrime.add(eopNew);
        if (!redo.isEmpty()) {
            eolPrime.add(IT(redo.get(0), eopNew));
            for (int i = 1; i < redo.size(); i++) {
                List<Op> his = new ArrayList<Op>(hBuffer.subList(m + 1, m + i));
                Collections.reverse(his);
                Op opTmp = LET(redo.get(i), his);
                eolPrime.add(LIT(opTmp, eolPrime));
            }
            for (int i = 0; i < redo.size(); i++)
                hBuffer.remove(hBuffer.size() - 1);
        }
        // redo eol Prime
        hBuffer.addAll(eolPrime);

        // todo now just show it out
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hBuffer.size(); i++) {
            if (hBuffer.get(i).getOpType() == OpType.INSERT_CHAR) {
                Insertion in = (Insertion) hBuffer.get(i);
                builder.insert(in.getPos(), in.getC());
            } else {
                Deletion del = (Deletion) hBuffer.get(i);
                builder.delete(del.pos, del.pos);
            }
        }
        System.out.println(builder.toString());
    }

    private static Op IT(Op a, Op b) {
        return ITransformation.IT(a, b);
    }

    private static Op LIT(Op op, List<Op> ops) {
        return ITransformation.LIT(op, ops);
    }

    private static Op LET(Op op, List<Op> ops) {
        return ETransformation.LET(op, ops);
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        OT.id = id;
    }

    public static void updateStateVec(int id) {
        stateVector.addOne(id);
    }

    public static StateVector getStateVector() {
        return stateVector;
    }

    public static void setStateVector(StateVector stateVector) {
        OT.stateVector = stateVector;
    }
}
