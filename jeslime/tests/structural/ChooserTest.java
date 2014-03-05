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

package structural;

import junit.framework.TestCase;

/**
 * Created by dbborens on 3/5/14.
 */
public class ChooserTest extends TestCase {
    public void testAdd() throws Exception {
        fail("Not yet implemented.");
    }

    public void testClose() throws Exception {
        fail("Not yet implemented.");
    }

    public void testSelectTarget() throws Exception {
        fail("Not yet implemented.");
    }

    public void testBinaryRangeSearch() throws Exception {
        fail("Not yet implemented.");
    }


//    private int doSearch(double x) {
//        // These are irrelevant -- just for mock
//        Integer[] arr = new Integer[0];
//        GillespieState gs = new GillespieState(arr);
//
//        // Set up the chooser
//        GillespieChooser chooser = new GillespieChooser(gs);
//        double[] bins = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
//
//        int result = chooser.binaryRangeSearch(0, 4, x, bins);
//
//        return result;
//    }
//
//    /**
//     * Test the binary range search on the GillespieChooser.
//     */
//    public void testBinaryRangeSearch() {
//
//        // Failure case
//        assertEquals(-1, doSearch(-0.5));
//
//        // Standard cases
//        assertEquals(0, doSearch(0.5));
//        assertEquals(1, doSearch(1.5));
//        assertEquals(2, doSearch(2.5));
//
//        // Edge case -- lower is inclusive
//        assertEquals(0, doSearch(0.0));
//
//        // Edge case -- upper is exclusive
//        assertEquals(-1, doSearch(5.0));
//
}

