/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
        LightweightSystemState state = new LightweightSystemState(geom);
        query.populate(state);
        assertTrue(state.isHighlighted(0, x));
        assertTrue(state.isHighlighted(0, y));
        assertFalse(state.isHighlighted(0, origin));
    }
}
