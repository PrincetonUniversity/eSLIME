/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
