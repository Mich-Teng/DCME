package util;

import com.chao.dcme.ot_char.StateVector;
import com.chao.dcme.util.Utilities;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-19 20:01.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class UtilitiesTest {
    @Test
    public void serializeAndDeserialize() {
        // null object
        byte[] bytes = Utilities.serialize(null);
        assertEquals("Null Test for Serialize", bytes, null);
        Object ob = Utilities.deserialize(null);
        assertEquals("Null Test for Deserialize", ob, null);

        // Test for Integer
        bytes = Utilities.serialize(1234);
        Integer target = (Integer) Utilities.deserialize(bytes);
        assertEquals("Test for Integer", target, (Integer) 1234);

        // test for Object
        StateVector stateVector = new StateVector(12);
        stateVector = (StateVector) Utilities.deserialize(
                Utilities.serialize(stateVector));
        assertEquals("Test for Object", stateVector.getId(), 12);
    }
}
