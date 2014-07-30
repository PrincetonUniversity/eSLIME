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

package io.deserialize;

import test.EslimeTestCase;

/**
 * Created by dbborens on 3/28/14.
 */
public class TimeReaderTest extends EslimeTestCase {

    private TimeReader query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new TimeReader(fixturePath);
    }

    public void testGetTimes() throws Exception {
        double[] expected = new double[]{0.5, 1.3};
        double[] actual = query.getTimes();
        assertArraysEqual(expected, actual, false);
    }

    public void testGetFrames() throws Exception {
        int[] expected = new int[]{2, 4};
        int[] actual = query.getFrames();
        assertArraysEqual(expected, actual, false);
    }
}
