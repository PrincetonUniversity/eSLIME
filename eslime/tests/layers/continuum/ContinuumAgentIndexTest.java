/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ContinuumAgentIndexTest {

    private Function<BehaviorCell, RelationshipTuple> lookup;
    private BehaviorCell cell;
    private RelationshipTuple relationship;

    private ContinuumAgentIndex query;

    @Before
    public void init() throws Exception {
        cell = mock(BehaviorCell.class);
        relationship = mock(RelationshipTuple.class);
        lookup = (Function<BehaviorCell, RelationshipTuple>) mock(Function.class);
        when(lookup.apply(any())).thenReturn(relationship);

        query = new ContinuumAgentIndex(lookup);
    }

    @Test
    public void addViaNotifier() throws Exception {
        ContinuumAgentNotifier notifier = query.getNotifier();
        notifier.add(cell);
        Stream<RelationshipTuple> actual = query.getRelationships();
        assertEquals(asList(relationship), actual.collect(Collectors.toList()));
    }

    @Test
    public void removeViaNotifier() throws Exception {
        ContinuumAgentNotifier notifier = query.getNotifier();
        notifier.add(cell);
        notifier.remove(cell);
        Stream<RelationshipTuple> actual = query.getRelationships();
        assertEquals(asList(), actual.collect(Collectors.toList()));
    }

    @Test
    public void resetClearsContents() throws Exception {
        ContinuumAgentNotifier notifier = query.getNotifier();
        notifier.add(cell);
        query.reset();
        Stream<RelationshipTuple> actual = query.getRelationships();
        assertEquals(asList(), actual.collect(Collectors.toList()));
    }

    @Test(expected = IllegalStateException.class)
    public void removeNonExistentThrows() throws Exception {
        ContinuumAgentNotifier notifier = query.getNotifier();
        notifier.remove(cell);
    }

    @Test(expected = IllegalStateException.class)
    public void addExistingThrows() throws Exception {
        ContinuumAgentNotifier notifier = query.getNotifier();
        notifier.add(cell);
        notifier.add(cell);
    }
}