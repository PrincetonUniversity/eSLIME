/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
