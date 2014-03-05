/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

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
        String fixture = fixturePath + "solute42.state.bin";
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
