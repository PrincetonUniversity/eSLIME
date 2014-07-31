/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;//import junit.framework.TestCase;

import test.EslimeTestCase;

public class BoundaryReachedEventTest extends EslimeTestCase {
    private BoundaryReachedEvent query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new BoundaryReachedEvent();
    }

    public void testGillespie() {
        query.setGillespie(1.0);
        assertEquals(1.0, query.getGillespie());
    }

    public void testToString() {
        assertEquals("BoundaryReachedEvent", query.toString());
    }
}