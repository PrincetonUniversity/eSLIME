/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import cells.MockCell;
import control.GeneralParameters;
import control.identifiers.Coordinate;
import layers.cell.CellUpdateManager;
import processes.StepState;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/28/14.
 */
public class CensusWriterTest extends EslimeLatticeTestCase {

    public void testLifeCycle() throws Exception {
        GeneralParameters p = makeMockGeneralParameters();
        CensusWriter writer = new CensusWriter(p);
        writer.init(layerManager);
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

        writer.dispatchHalt(null);
        writer.close();
        assertFilesEqual("census.txt");
    }

    /**
     * Verifies that two cycles of the CensusWriter do not affect one another.
     * Regression test for bug 981770-71079096.
     *
     */
    public void testCycleIndependence() throws Exception {
        MockGeneralParameters p = makeMockGeneralParameters();
        p.setInstancePath(outputPath + "censusWriterTest/1/");
        CensusWriter writer = new CensusWriter(p);
        writer.init(layerManager);
        StepState state = new StepState(0.0, 0);
        put(origin, 1);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(null);

        p.setInstancePath(outputPath + "censusWriterTest/2/");
        writer.init(layerManager);
        // Create original configuration
        replace(origin, 2);
        state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(null);
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
