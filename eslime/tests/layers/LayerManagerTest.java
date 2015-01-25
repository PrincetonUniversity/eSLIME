/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import layers.cell.CellLayer;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.ContinuumLayer;
import org.junit.Before;
import org.junit.Test;
import processes.StepState;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by David B Borenstein on 12/30/13.
 */
public class LayerManagerTest {

    private LayerManager query;
    private ContinuumLayer continuumLayer;
    private CellLayer cellLayer;
    private String id;

    @Before
    public void init() {
        ContinuumAgentLinker linker = mock(ContinuumAgentLinker.class);
        id = "test";

        continuumLayer = mock(ContinuumLayer.class);
        when(continuumLayer.getLinker()).thenReturn(linker);
        when(continuumLayer.getId()).thenReturn(id);

        cellLayer = mock(CellLayer.class);
        query = new LayerManager();
    }

    @Test
    public void addContinuumLayer() throws Exception {
        query.addContinuumLayer(continuumLayer);
        assertEquals(continuumLayer, query.getContinuumLayer(id));
    }

    @Test
    public void resetClearsCellLayer() throws Exception {
        query.setCellLayer(cellLayer);
        query.reset();
        verify(cellLayer).reset();
    }

    @Test
    public void resetClearsContinuumLayer() throws Exception {
        query.addContinuumLayer(continuumLayer);
        query.reset();
        verify(continuumLayer).reset();
    }

    @Test
    public void stepState() throws Exception {
        StepState stepState = mock(StepState.class);
        query.setStepState(stepState);
        assertEquals(stepState, query.getStepState());
    }

    @Test
    public void cellLayer() throws Exception {
        query.setCellLayer(cellLayer);
        assertEquals(cellLayer, query.getCellLayer());
    }
}
