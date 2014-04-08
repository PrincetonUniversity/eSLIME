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

import control.identifiers.Coordinate;
import layers.LightweightSystemState;
import test.EslimeLatticeTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by dbborens on 3/26/14.
 */
public class HighlightReaderTest extends EslimeLatticeTestCase {

    private HighlightReader query;
    private MockCoordinateDeindexer deindexer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String root = fixturePath;
        int[] channels = new int[]{0};
        deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(cc);
        query = new HighlightReader(root, channels, deindexer);
    }

    public void testNext() throws Exception {
        Map<Integer, Set<Coordinate>> actual = query.next();

        // First, make sure that the only highlight channel is channel 0.
        assertEquals(1, actual.size());
        assertTrue(actual.containsKey(0));

        // Then, check what coordinates were highlighted. To see the
        // definition of the fixture, see HighlightWriterTest.
        Set<Coordinate> highlights = actual.get(0);
        assertEquals(2, highlights.size());
        assertTrue(highlights.contains(x));
        assertTrue(highlights.contains(y));
        assertFalse(highlights.contains(origin));
    }

    public void testPopulate() throws Exception {
        LightweightSystemState state = new LightweightSystemState(deindexer);
        query.populate(state);
        assertTrue(state.isHighlighted(0, x));
        assertTrue(state.isHighlighted(0, y));
        assertFalse(state.isHighlighted(0, origin));
    }
}
