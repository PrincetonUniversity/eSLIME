/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import test.EslimeTestCase;

import java.util.Collection;
import java.util.HashSet;

public class RelationshipManagerTest extends EslimeTestCase {

    private RelationshipManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new RelationshipManager();
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
        assertEquals(0.0, query.getInj("foo"), epsilon);
        query.setInj("foo", 1.0);
        assertEquals(1.0, query.getInj("foo"), epsilon);
    }

    public void testSetGetExp() throws Exception {
        assertEquals(0.0, query.getExp("foo"), epsilon);
        query.setExp("foo", 1.0);
        assertEquals(1.0, query.getExp("foo"), epsilon);
    }
}