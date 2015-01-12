/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;

import java.util.IdentityHashMap;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

public class ContinuumAgentIndexTest {

    private IdentityHashMap<BehaviorCell, Supplier<RelationshipTuple>> map;
    private BehaviorCell cell;
    private Supplier<RelationshipTuple> supplier;
    private ContinuumAgentIndex query;

    @Before
    public void init() throws Exception {
        map = (IdentityHashMap<BehaviorCell, Supplier<RelationshipTuple>>) mock(IdentityHashMap.class);
        cell = mock(BehaviorCell.class);
        supplier = (Supplier<RelationshipTuple>) mock(Supplier.class);
        query = new ContinuumAgentIndex(map);

    }

    @Test
    public void add() throws Exception {
        query.getNotifier().add(cell, supplier);
        verify(map).put(cell, supplier);
    }

    @Test(expected = IllegalStateException.class)
    public void addExistingThrows() throws Exception {
        when(map.containsKey(any())).thenReturn(true);
        query.getNotifier().add(cell, supplier);
    }

    @Test
    public void remove() throws Exception {
        when(map.containsKey(any())).thenReturn(true);
        query.getNotifier().remove(cell);
        verify(map).remove(cell);
    }

    @Test(expected = IllegalStateException.class)
    public void removeAbsentThrows() throws Exception {
        when(map.containsKey(any())).thenReturn(false);
        query.getNotifier().remove(cell);
    }


    // I can't figure out how to mock this. Revisit later.
//    @Test
//    public void getRelationships() throws Exception {
//        map = new IdentityHashMap<>();
//        query = new ContinuumAgentIndex(map);
//        map.put(cell, supplier);
//        when(supplier.get()).thenReturn(tuple);
//
//        Stream<RelationshipTuple> expected = Stream.of(tuple);
//        Stream<RelationshipTuple> actual = query.getRelationships();
//        assertEquals(expected, actual);
//    }
}