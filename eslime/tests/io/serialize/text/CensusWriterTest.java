/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import cells.MockCell;
import control.halt.ManualHaltEvent;
import control.identifiers.Coordinate;
import layers.cell.CellUpdateManager;
import processes.StepState;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/28/14.
 */
public class CensusWriterTest extends EslimeLatticeTestCase {
    private MockGeneralParameters p;
    private CensusWriter writer;
    private ManualHaltEvent haltEvent;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
        writer = new CensusWriter(p, layerManager);
        haltEvent = new ManualHaltEvent("");
    }

    public void testLifeCycle() throws Exception {
        writer.init();
        // Create original configuration
        put(origin, 1);
        put(x, 1);
        put(y, 2);
        put(z, 3);

        // Flush original configuration
        StepState state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);

        // Create second configuration
        replace(origin, 3);
        state = new StepState(1.0, 1);
        state.record(cellLayer);
        writer.flush(state);

        // Create third configuration
        put(yz, 4);
        state = new StepState(2.0, 2);
        state.record(cellLayer);
        writer.flush(state);

        haltEvent.setGillespie(2.0);
        writer.dispatchHalt(haltEvent);
        writer.close();
        assertFilesEqual("census.txt");
    }

    /**
     * Verifies that two cycles of the CensusWriter do not affect one another.
     * Regression test for bug 981770-71079096.
     *
     */
    public void testCycleIndependence() throws Exception {
        haltEvent.setGillespie(0);
        p.setInstancePath(outputPath + "censusWriterTest/1/");
        writer.init();
        StepState state = new StepState(0.0, 0);
        put(origin, 1);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(haltEvent);

        p.setInstancePath(outputPath + "censusWriterTest/2/");
        writer.init();
        // Create original configuration
        replace(origin, 2);
        state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(haltEvent);
        writer.close();

        assertFilesEqual("censusWriterTest/1/census.txt");
        assertFilesEqual("censusWriterTest/2/census.txt");
    }
    private void replace(Coordinate c, int state) throws Exception {
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.banish(c);
        put(c, state);
    }

    private void put(Coordinate c, int state) throws Exception {
        MockCell cell = new MockCell(state);
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.place(cell, c);
    }
}
