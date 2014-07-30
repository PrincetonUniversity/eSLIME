/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import test.EslimeTestCase;

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
        double[] actual = csr.next();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        assertArraysEqual(expected, actual, false);

        // Second record
        actual = csr.next();
        expected = new double[]{0.0, 0.1, 0.2, 0.3, 0.4};
        assertArraysEqual(expected, actual, false);
    }

    @Override
    public void tearDown() {
        csr.close();
    }
}
