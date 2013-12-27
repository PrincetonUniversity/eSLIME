package io.deserialize;

import test.EslimeTestCase;
import structural.ContinuumState;

import java.io.File;

/**
 * Created by dbborens on 12/11/13.
 */
public class ContinuumStateReaderTest extends EslimeTestCase {

    private ContinuumStateReader csr;

    @Override
    public void setUp() {
        String fixture = fixturePath + "solute42.state.txt";
        File fixtureFile = new File(fixture);
        csr = new ContinuumStateReader(fixtureFile);
    }

    public void testHasNext() throws Exception {
        // There are two records in the fixture file.
        assertTrue(csr.hasNext());
        csr.next();
        assertTrue(csr.hasNext());
        csr.next();
        assertFalse(csr.hasNext());
    }

    public void testNext() throws Exception {
        // Look at first record
        ContinuumState state = csr.next();
        double[] values = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        assertStatesEqual(state, 1, 1.0, values);

        // Second record
        state = csr.next();
        values = new double[] {0.0, 0.1, 0.2, 0.3, 0.4};
        assertStatesEqual(state, 2, 2.0, values);
    }

    public void assertStatesEqual(ContinuumState state, int frame, double gillespie, double[] values) {
        assertEquals(frame, state.getFrame());
        assertEquals(gillespie, state.getGillespie());
        assertEquals(values.length, state.getData().size());

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], state.getData().get(i));
        }
    }
    @Override
    public void tearDown() {
        csr.close();
    }
}
