/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.identifiers.Coordinate;
import layers.continuum.RelationshipTuple;
import test.EslimeTestCase;

import java.util.Collection;
import java.util.HashSet;

public class AgentContinuumManagerTest extends EslimeTestCase {

    private AgentContinuumManager query;
    private Coordinate c;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Coordinate(1, 2, 3);
        SelfLocator locator = () -> c;
        query = new AgentContinuumManager(locator);
    }

    public void testGetRelationships() throws Exception {
        query.setExp("foo", 1.0);
        query.setInj("bar", 1.0);

        Collection<String> actual = query.getRelationships();
        Collection<String> expected = new HashSet<>();
        expected.add("foo");
        expected.add("bar");
        assertCollectionsEqual(expected, actual);
    }

    public void testSetGetInj() throws Exception {
        RelationshipTuple expected = new RelationshipTuple(c, 0.0);
        assertEquals(expected, query.getInj("foo"));

        query.setInj("foo", 1.0);
        expected = new RelationshipTuple(c, 1.0);
        assertEquals(expected, query.getInj("foo"));
    }

    public void testSetGetExp() throws Exception {
        RelationshipTuple expected = new RelationshipTuple(c, 0.0);
        assertEquals(expected, query.getExp("foo"));

        query.setExp("foo", 1.0);
        expected = new RelationshipTuple(c, 1.0);
        assertEquals(expected, query.getExp("foo"));
    }

}