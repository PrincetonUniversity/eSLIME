/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import cells.MockCell;
import control.identifiers.Coordinate;
import test.EslimeTestCase;

import java.util.HashMap;

public class ContinuumAgentIndexTest extends EslimeTestCase {

    private ContinuumAgentIndex query;
    private BehaviorCell cell;
    private RelationshipTuple tuple;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        cell = new MockCell();
        tuple = new RelationshipTuple(new Coordinate(1, 2, 3), 1.0);
        AgentRelationshipLookup lookup = cell -> tuple;
        query = new ContinuumAgentIndex(lookup);
    }

    public void testLifeCycle() {
        // Get relationship set -- should be empty
        HashMap<Coordinate, Double> expected = new HashMap<>(1);
        assertMapsEqual(expected, query.getRelationShips());

        // Add a relationship
        query.add(cell);

        // Get relationship set -- should be there
        expected.put(tuple.getCoordinate(), tuple.getMagnitude());
        assertMapsEqual(expected, query.getRelationShips());

        // Remove that relationship
        query.remove(cell);

        // Get relationship set -- should be empty
        expected.remove(tuple.getCoordinate());
        assertEquals(expected, query.getRelationShips());
    }

    public void testReset() {
        query.add(cell);
        query.reset();
        HashMap<Coordinate, Double> expected = new HashMap<>(1);
        assertEquals(expected, query.getRelationShips());
    }
}