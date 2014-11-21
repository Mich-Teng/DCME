package ot_char;

import com.chao.dcme.ot_char.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-19 20:11.
 * Package: ot_char
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * the examples are modified from the paper "Achieving Convergence, Causality,
 * Preservation, and Intention Preservation in Real-Time Cooperative Editing
 * System", figure 2
 */
public class OTTest {
    StateVector v1 = new StateVector(0);
    StateVector v2 = new StateVector(1);
    StateVector v3 = new StateVector(1);
    StateVector v4 = new StateVector(2);
    Op op1 = new Deletion(v1, 2);
    Op op2 = new Insertion(v2, 4, 'a');
    Op op3 = new Deletion(v3, 5);
    Op op4 = new Deletion(v4, 6);

    JTextArea textArea = new JTextArea("ABCDEFGH");

    @Before
    public void init() {
        v1.addOne(0);
        v2.addOne(1);
        v3.addOne(1);
        v3.addOne(1);
        v3.addOne(0);
        v4.addOne(1);
        v4.addOne(2);
    }

    @Test
    public void GOT() {

        // site 0, no need undo and redo, just GOT
        OT.init(0, "ABCDEFGH", textArea);
        Op newOp = OT.GOT(op1);
        OT.apply(newOp);
        assertEquals("[GOT]Test for op1", ((Deletion) newOp).getPos(), 2);
        OT.addIntoBuffer(newOp);
        newOp = OT.GOT(op2);
        OT.apply(newOp);
        assertEquals("[GOT]Test for op2", ((Insertion) newOp).getPos(), 3);
        OT.addIntoBuffer(newOp);
        newOp = OT.GOT(op4);
        OT.apply(newOp);
        assertEquals("[GOT]Test for op4", ((Deletion) newOp).getPos(), 5);
        OT.addIntoBuffer(newOp);
        newOp = OT.GOT(op3);
        OT.apply(newOp);
        assertEquals("[GOT]Test for op3", newOp, null);
        assertEquals("[GOT]Test for Final Result", textArea.getText(), "ABDaEGH");
    }

    @Test
    public void transform() {
        // site 1, test transform
        OT.init(1, "ABCDEFGH", textArea);
        OT.transform(op2);
        assertEquals("[Transform]Test for op2", textArea.getText(), "ABCDaEFGH");
        OT.transform(op1);
        assertEquals("[Transform]Test for op1", textArea.getText(), "ABDaEFGH");
        OT.transform(op3);
        assertEquals("[Transform]Test for op3", textArea.getText(), "ABDaEGH");
        OT.transform(op4);
        assertEquals("[Transform]Test for op4", textArea.getText(), "ABDaEGH");
    }




}
