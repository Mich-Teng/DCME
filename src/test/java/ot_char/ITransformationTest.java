package ot_char;

import com.chao.dcme.ot_char.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-19 20:12.
 * Package: ot_char
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ITransformationTest {
    @Test
    public void ii() {
        StateVector stateVector = new StateVector(0);
        Insertion a = new Insertion(stateVector, 2, 'c');
        Insertion b = new Insertion(stateVector, 1, 'b');
        Insertion ret = ITransformation.ii(a, b);
        assertEquals("Test for ii", ret.getPos(), 3);
        assertEquals("Test for ii", ret.getC(), 'c');
    }

    @Test
    public void dd() {
        StateVector stateVector = new StateVector(0);
        Deletion a = new Deletion(stateVector, 2);
        Deletion b = new Deletion(stateVector, 1);
        Deletion ret = ITransformation.dd(a, b);
        assertEquals("Test for dd", ret.getPos(), 1);
    }

    @Test
    public void id() {
        StateVector stateVector = new StateVector(0);
        Insertion a = new Insertion(stateVector, 2, 'c');
        Deletion b = new Deletion(stateVector, 2);
        Insertion ret = ITransformation.id(a, b);
        assertEquals("Test for id", ret.getPos(), 2);
        assertEquals("Test for id", ret.getC(), 'c');
    }

    @Test
    public void di() {
        StateVector stateVector = new StateVector(0);
        Deletion a = new Deletion(stateVector, 2);
        Insertion b = new Insertion(stateVector, 1, 'b');
        Deletion ret = ITransformation.di(a, b);
        assertEquals("Test for di", ret.getPos(), 3);
    }

    @Test
    public void lit() {
        StateVector stateVector = new StateVector(0);
        Deletion a = new Deletion(stateVector, 2);
        Insertion b = new Insertion(stateVector, 1, 'b');
        Deletion c = new Deletion(stateVector, 2);
        Insertion d = new Insertion(stateVector, 4, 'b');
        Deletion e = new Deletion(stateVector, 0);
        List<Op> ops = new ArrayList<Op>();
        ops.add(b);
        ops.add(c);
        ops.add(d);
        ops.add(e);
        Op ret = ITransformation.LIT(a, ops);
        assertEquals("Test for LIT", ((Deletion) ret).getPos(), 1);
    }
}
