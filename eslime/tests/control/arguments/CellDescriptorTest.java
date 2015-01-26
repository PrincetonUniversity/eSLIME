/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.Behavior;
import cells.BehaviorCell;
import control.GeneralParameters;
import control.identifiers.Coordinate;
import factory.cell.Reaction;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.CellLayerViewer;
import layers.cell.CellLookupManager;
import layers.cell.CellUpdateManager;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.ContinuumAgentNotifier;
import layers.continuum.ContinuumLayer;
import org.junit.*;
import test.TestBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * TODO The sheer number of mocks required tells you that this class needs refactoring
 */
public class CellDescriptorTest extends TestBase {

    private Argument<Double> threshold;
    private Argument<Double> initialHealth;
    private Argument<Integer> cellState;
    private Reaction reaction1, reaction2;
    private BehaviorDescriptor behaviorDescriptor;
    private Behavior behavior;

    private CellDescriptor query;

    @Before
    public void init() throws Exception {
        threshold = new ConstantDouble(0.5);
        initialHealth = new ConstantDouble(0.75);
        cellState = new ConstantInteger(1);

        // TODO Assignment of reactions should be handled by a helper
        reaction1 = mock(Reaction.class);
        when(reaction1.getId()).thenReturn("1");

        reaction2 = mock(Reaction.class);
        when(reaction2.getId()).thenReturn("2");

        Stream<Reaction> reactions = Arrays.asList(new Reaction[] {reaction1, reaction2}).stream();

        // TODO Instantiation of behaviors from descriptors should be handled by a helper
        behaviorDescriptor = mock(BehaviorDescriptor.class);
        behavior = mock(Behavior.class);
        when(behaviorDescriptor.instantiate(any())).thenReturn(behavior);
        Map<String, BehaviorDescriptor> behaviorDescriptors = new HashMap<>(1);
        behaviorDescriptors.put("behavior", behaviorDescriptor);

        GeneralParameters p = mock(GeneralParameters.class);
        LayerManager layerManager = mock(LayerManager.class);

        // TODO: Refactor CellLayer hierarchy (86866040)
        // There are too many calls -- these things should
        // all be handled in child objects that can be mocked.
        CellLayer cellLayer = mock(CellLayer.class);
        CellLayerViewer viewer = mock(CellLayerViewer.class);
        when(viewer.exists(any())).thenReturn(true);
        when(cellLayer.getViewer()).thenReturn(viewer);
        when(layerManager.getCellLayer()).thenReturn(cellLayer);
        when(layerManager.getCellLayer().getViewer().exists(any())).thenReturn(true);
        CellLookupManager lookup = mock(CellLookupManager.class);
        when(lookup.getCellLocation(any())).thenReturn(mock(Coordinate.class));
        when(cellLayer.getLookupManager()).thenReturn(lookup);
        when(cellLayer.getUpdateManager()).thenReturn(mock(CellUpdateManager.class));
        ContinuumLayer continuumLayer = mock(ContinuumLayer.class);
        ContinuumAgentLinker linker = mock(ContinuumAgentLinker.class);
        when(linker.getNotifier()).thenReturn(mock(ContinuumAgentNotifier.class));
        when(continuumLayer.getLinker()).thenReturn(linker);
        when(layerManager.getContinuumLayer(any())).thenReturn(continuumLayer);

        query = new CellDescriptor(layerManager, p);
        query.setCellState(cellState);
        query.setInitialHealth(initialHealth);
        query.setThreshold(threshold);
        query.setReactions(reactions);
        query.setBehaviorDescriptors(behaviorDescriptors);
    }

    @Test
    public void cellState() throws Exception {
        BehaviorCell result = query.next();
        assertEquals((int) cellState.next(), result.getState());
    }

    @Test
    public void threshold() throws Exception {
        BehaviorCell result = query.next();
        assertEquals(threshold.next(), result.getThreshold(), epsilon);
    }

    @Test
    public void initialHealth() throws Exception {
        BehaviorCell result = query.next();
        assertEquals(initialHealth.next(), result.getHealth(), epsilon);
    }

    // TODO this should be tested directly
    @Test
    public void reactions() throws Exception {
        BehaviorCell result = query.next();
        List<String> expected = Arrays.asList(new String[] {"1", "2"});

        assertEquals(expected, result.getReactionIds().collect(Collectors.toList()));
    }

    // TODO this should be tested directly
    @Test
    public void behaviors() throws Exception {
        BehaviorCell result = query.next();
        List<String> expected = Stream.of("behavior").collect(Collectors.toList());
        verify(behaviorDescriptor).instantiate(any());
        assertEquals(expected, result.getBehaviorNames().collect(Collectors.toList()));
    }

}