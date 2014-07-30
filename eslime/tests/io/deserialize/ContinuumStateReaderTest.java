/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
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
