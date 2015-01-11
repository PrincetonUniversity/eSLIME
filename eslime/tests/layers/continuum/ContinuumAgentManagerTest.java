/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.LinearMocks;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ContinuumAgentManagerTest extends LinearMocks {

    private ContinuumAgentIndex injIndex, expIndex;

    private ReactionLoader loader;
    private String id;

    private ContinuumAgentManager query;
    private Function<Coordinate, Double> stateLookup;


    @Before
    public void init() {
        id = "test";
        loader = mock(ReactionLoader.class);

        injIndex = makeMockIndex();
        expIndex = makeMockIndex();

        stateLookup = (Function<Coordinate, Double>) mock(Function.class);
        when(stateLookup.apply(any())).thenReturn(-1.0);
        query = new ContinuumAgentManager(loader, injIndex, expIndex, id);
    }

    @Test
    public void applySchedulesInjection() {
        query.apply();
        Stream<RelationshipTuple> expected = injIndex.getRelationships();
        ArgumentCaptor<? extends Stream<RelationshipTuple>> captor = ArgumentCaptor.forClass(Stream.class);
        verify(loader).inject(captor.capture());
        assertTrue(expected == captor.getValue());
    }

    @Test
    public void applySchedulesExponentiation() {
        query.apply();
        Stream<RelationshipTuple> expected = expIndex.getRelationships();
        ArgumentCaptor<? extends Stream<RelationshipTuple>> captor = ArgumentCaptor.forClass(Stream.class);
        verify(loader).exponentiate(captor.capture());
        assertTrue(expected == captor.getValue());
    }

    @Test
    public void resetCallsInjIndex() {
        query.reset();
        verify(injIndex).reset();
    }

    @Test
    public void resetCallsExpIndex() {
        query.reset();
        verify(expIndex).reset();
    }

    @Test
    public void linkerHasInjNotifier() {
        ContinuumAgentLinker linker = query.getLinker(stateLookup);
        ContinuumAgentNotifier expected = injIndex.getNotifier();
        assertTrue(expected == linker.getInjNotifier());
    }

    @Test
    public void linkerHasExpNotifier() {
        ContinuumAgentLinker linker = query.getLinker(stateLookup);
        ContinuumAgentNotifier expected = expIndex.getNotifier();
        assertTrue(expected == linker.getExpNotifier());
    }

    @Test
    public void linkerHasStateLookup() {
        ContinuumAgentLinker linker = query.getLinker(stateLookup);
        linker.get(a);
        verify(stateLookup).apply(a);
    }

    private ContinuumAgentIndex makeMockIndex() {
        ContinuumAgentIndex index = mock(ContinuumAgentIndex.class);

        Stream<RelationshipTuple> relationships = (Stream<RelationshipTuple>) mock(Stream.class);
        when(index.getRelationships()).thenReturn(relationships);

        ContinuumAgentNotifier notifier = mock(ContinuumAgentNotifier.class);
        when(index.getNotifier()).thenReturn(notifier);

        return index;
    }

}