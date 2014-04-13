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

package structural;

import cells.MockCell;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 4/11/14.
 */
public class CanonicalCellMapTest extends EslimeTestCase {
    private CanonicalCellMap query;
    private Coordinate c, nc, d;
    private MockCell cell;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new CanonicalCellMap();
        c = new Coordinate(0, 0, 0);
        nc = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        d = new Coordinate(0, 0, 0, 0);

        cell = new MockCell(1);
    }

    public void testPutGet() throws Exception {
        query.put(c, cell);
        assertEquals(cell, query.get(c));
        assertEquals(cell, query.get(nc));
        assertEquals(null, query.get(d));
    }

    public void testContainsKey() throws Exception {
        query.put(c, cell);
        assertTrue(query.containsKey(c));
        assertTrue(query.containsKey(nc));
        assertFalse(query.containsKey(d));
    }
}
