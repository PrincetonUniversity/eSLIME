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

import layers.LightweightSystemState;
import test.EslimeLatticeTestCase;

/**
 * Test for the SystemStateReader. As an I/O orchestrator whose main function
 * is to open a bunch of files, this is an annoying class to mock. Ultimately,
 * I decided (for the time being) to use existing mocks, some of which are
 * incompatible. As a result, this is a rather cursory test. If I start running
 * into problems surrounding deserialization, I will rewrite the tests for this
 * class.
 * <p/>
 * Created by dbborens on 3/28/14.
 */
public class SystemStateReaderTest extends EslimeLatticeTestCase {
    private SystemStateReader query;
    private String[] soluteIds;
    private int[] channelIds;

    @Override
    protected void setUp() throws Exception {
        soluteIds = new String[]{"42", "43"};
        channelIds = new int[]{0};

        query = new SystemStateReader(soluteIds, channelIds, fixturePath);
    }

    public void testHasNext() throws Exception {
        // There are two frames specified in the time fixture, so we expect
        // that hasNext() will return true.
        assertTrue(query.hasNext());

        query.next();
        assertTrue(query.hasNext());

        // Ideally, we'd like to run next() one more time and see that it
        // returns false. However, not all of the fixtures have a second frame,
        // and it didn't seem like a subtle enough problem to warrant a whole
        // set of fixtures just for perfect coverage.
    }

    public void testNext() throws Exception {
        LightweightSystemState state = query.next();

        fail("Not yet implemented");
    }

    /**
     * For solutes, we can use solute42 and solute43, which are predefined
     * fixtures. For everything else, it pays to generate new stubs.
     */
    private void generateExpected() {
    }
}
