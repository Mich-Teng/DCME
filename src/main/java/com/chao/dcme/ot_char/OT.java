package com.chao.dcme.ot_char;

import java.util.*;

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
    private static List<Op> pendingBuffer = new ArrayList<Op>();

    private static StringBuilder builder = null;
    private static String initialStr = "";


    public static void addIntoPendingBuffer(Op op) {
        pendingBuffer.add(op);
    }

    public static void checkPendingBuffer() {
        Iterator<Op> iter = pendingBuffer.iterator();
        while (iter.hasNext()) {
            Op op = iter.next();
            if (isCausallyReady(op)) {
                transform(op);
                iter.remove();
            }
        }
    }

    public static String getText() {
        return builder.toString();
    }

    /**
     * check whether this op is causally ready
     *
     * @param op operation
     * @return true or false
     */
    public static boolean isCausallyReady(Op op) {
        StateVector vec = op.getStateVec();
        int s = vec.getId();
        if (!(vec.get(s) == stateVector.get(s) + 1))
            return false;
        for (int i = 0; i < stateVector.size(); i++) {
            if (i != s && vec.get(i) > stateVector.get(i))
                return false;
        }
        return true;
    }

    public static void init(int idNum, String str) {
        id = idNum;
        stateVector = new StateVector(id);
        initialStr = str;
        builder = new StringBuilder(str);
        hBuffer.clear();
        pendingBuffer.clear();
    }

    // whether Oa --> Ob
    private static boolean isDependent(StateVector a, StateVector b, int i) {
        // if b[id] >= a[id], it means that before b is generated, b has the
        // knowledge of a[id], so b is dependent on a
        return a.get(i) <= b.get(i);
    }

    private static boolean isTotalOrdering(StateVector v1, StateVector v2) {
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < v1.size(); i++) {
            sum1 += v1.get(i);
            sum2 += v2.get(i);
        }
        return sum1 < sum2 || (sum1 == sum2 && v1.getId() < v2.getId());
    }

    // Give a new causally ready operation Onew, and HB, return the excution form
    // of Onew. Intention-preserving scheme
    public static Op GOT(Op newOp) {
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
            return LIT(newOp, hBuffer.subList(k, hBuffer.size()));
        }
        List<Op> eolPrime = new ArrayList<Op>();
        for (int i = 0; i < eol.size(); i++) {
            int ci = c.get(i);
            List<Op> his = new ArrayList<Op>(hBuffer.subList(k, ci));
            Collections.reverse(his);
            Op opTmp = LET(eol.get(i), his);
            eolPrime.add(LIT(opTmp, eolPrime));
        }
        Collections.reverse(eolPrime);
        Op onewPrime = LET(newOp, eolPrime);
        return LIT(onewPrime, hBuffer.subList(k, hBuffer.size()));
    }

    public static void transform(Op opNew) {
        List<Op> redo = new ArrayList<Op>();
        StateVector vecNew = opNew.getStateVec();
        for (int i = hBuffer.size() - 1; i >= 0; i--) {
            Op op = hBuffer.get(i);
            StateVector vecTmp = op.getStateVec();
            if (!isTotalOrdering(vecTmp, vecNew)) {
                redo.add(op);
                // undo the operations
                Op inverse = op.inverse();
                apply(inverse);
            } else {
                break;
            }
        }
        int m = hBuffer.size() - 1 - redo.size();
        // remove the last $count operations

        Op eopNew = GOT(opNew);

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
        // apply to the doc
        for (Op tmp : eolPrime)
            apply(tmp);
        // redo eol Prime
        hBuffer.addAll(eolPrime);
    }

    public static void apply(Op op) {
        if (op == null)
            return;
        if (op.getOpType() == OpType.INSERT_CHAR) {
            Insertion insertion = (Insertion) op;
            builder.insert(insertion.getPos(), insertion.getC());
        } else {
            Deletion deletion = (Deletion) op;
            char c = builder.charAt(deletion.getPos());
            deletion.setC(c);
            builder.delete(deletion.getPos(), deletion.getPos()+1);
        }
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

    public static List<Op> gethBuffer() {
        return hBuffer;
    }

    public static void sethBuffer(List<Op> hBuffer) {
        OT.hBuffer = hBuffer;
    }

    public static void addIntoBuffer(Op op) {
        hBuffer.add(op);
    }

    public static String getInitialStr() {
        return initialStr;
    }

    public static void setInitialStr(String initialStr) {
        OT.initialStr = initialStr;
    }

    public static List<Op> getPendingBuffer() {
        return pendingBuffer;
    }

    public static void setPendingBuffer(List<Op> pendingBuffer) {
        OT.pendingBuffer = pendingBuffer;
    }
}
