/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
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
    public void testCycleIndependence() {
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
    private void replace(Coordinate c, int state) {
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.banish(c);
        put(c, state);
    }

    private void put(Coordinate c, int state) {
        MockCell cell = new MockCell(state);
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.place(cell, c);
    }
}
