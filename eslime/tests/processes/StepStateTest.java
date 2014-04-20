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

package processes;

import control.identifiers.Coordinate;
import test.EslimeLatticeTestCase;

/**
 * Created by David B Borenstein on 4/20/14.
 */
public class StepStateTest extends EslimeLatticeTestCase {

    private StepState query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new StepState(1.2);
    }

    public void testHighlight() throws Exception {
        query.highlight(origin, 1);

        Coordinate[] expected = new Coordinate[]{origin};
        Coordinate[] actual = query.getHighlights(1);
        assertArraysEqual(expected, actual, false);
    }

    public void testEmptyHighlight() throws Exception {
        query.highlight(origin, 1);

        Coordinate[] expected = new Coordinate[0];
        Coordinate[] actual = query.getHighlights(2);
        assertArraysEqual(expected, actual, false);
    }

    public void testDt() throws Exception {
        assertEquals(0.0, query.getDt(), epsilon);
        query.advanceClock(0.1);
        assertEquals(0.1, query.getDt(), epsilon);
    }

    public void testGetTime() throws Exception {
        assertEquals(1.2, query.getTime(), epsilon);
        query.advanceClock(0.1);
        assertEquals(1.3, query.getTime(), epsilon);
    }
}
