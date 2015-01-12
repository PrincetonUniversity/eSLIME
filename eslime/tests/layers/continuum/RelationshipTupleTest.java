/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import factory.cell.Reaction;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RelationshipTupleTest extends LinearMocks {

    private RelationshipTuple query;
    private Reaction reaction;

    @Before
    public void init() throws Exception {
        reaction = mock(Reaction.class);
        when(reaction.getExp()).thenReturn(1.0);
        when(reaction.getInj()).thenReturn(2.0);
        query = new RelationshipTuple(c, reaction);
    }

    @Test
    public void getCoordinate() throws Exception {
        assertEquals(c, query.getCoordinate());
    }

    public void getExp() throws Exception {
        assertEquals(1.0, query.getExp(), epsilon);
    }

    public void getInj() throws Exception {
        assertEquals(2.0, query.getInj(), epsilon);
    }
}