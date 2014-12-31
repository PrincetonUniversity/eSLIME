/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import test.EslimeTestCase;

public class RelationshipTupleTest extends EslimeTestCase {

    private RelationshipTuple query;
    private Coordinate c;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Coordinate(1, 2, 3);
        query = new RelationshipTuple(c, 1.0);
    }

    public void testGetCoordinate() throws Exception {
        assertEquals(c, query.getCoordinate());
    }

    public void testGetMagnitude() throws Exception {
        assertEquals(1.0, query.getMagnitude(), epsilon);
    }
}