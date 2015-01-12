/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ContinuumAgentManagerTest extends LinearMocks {

    private ContinuumAgentIndex index;
    private ReactionLoader loader;
    private String id;

    private ContinuumAgentManager query;

    @Before
    public void init() {
        id = "test";
        index = mock(ContinuumAgentIndex.class);

        loader = mock(ReactionLoader.class);

        query = new ContinuumAgentManager(loader, index, id);
    }

    @Test
    public void applyLoadsRelationshipsFromIndex() {
        Stream<RelationshipTuple> relationships = (Stream<RelationshipTuple>) mock(Stream.class);
        when(index.getRelationships()).thenReturn(relationships);
        query.apply();
        verify(loader).apply(relationships);
    }


    @Test
    public void resetCallsIndex() {
        query.reset();
        verify(index).reset();
    }

    public void getId() {
        assertEquals(id, query.getId());
    }
}