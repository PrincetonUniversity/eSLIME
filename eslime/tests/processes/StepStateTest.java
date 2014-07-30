/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes;

import cells.MockCell;
import control.identifiers.Coordinate;
import io.serialize.MockSerializationManager;
import layers.cell.CellLayer;
import layers.cell.CellUpdateManager;
import test.EslimeLatticeTestCase;

/**
 * Created by David B Borenstein on 4/20/14.
 */
public class StepStateTest extends EslimeLatticeTestCase {

    private StepState query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new StepState(1.2, 1);
    }

    public void testHighlight() throws Exception {
        query.highlight(origin, 1);

        Coordinate[] expected = new Coordinate[]{origin};
        Coordinate[] actual = query.getHighlights(1);
        assertArraysEqual(expected, actual, false);
    }

    public void testEmptyHighlight() throws Exception {
        query.highlight(origin, 1);

        Coordinate[] expected = new Coordinate[0];
        Coordinate[] actual = query.getHighlights(2);
        assertArraysEqual(expected, actual, false);
    }

    public void testDt() throws Exception {
        assertEquals(0.0, query.getDt(), epsilon);
        query.advanceClock(0.1);
        assertEquals(0.1, query.getDt(), epsilon);
    }

    public void testGetTime() throws Exception {
        assertEquals(1.2, query.getTime(), epsilon);
        query.advanceClock(0.1);
        assertEquals(1.3, query.getTime(), epsilon);
    }

    public void testGetFrame() throws Exception {
        assertEquals(1, query.getFrame());
    }

    public void testRecord() throws Exception {
        query.record(cellLayer);
        CellLayer actual = query.getRecordedCellLayer();
        assertEquals(cellLayer, actual);
        assertFalse(actual == cellLayer);
    }

    /**
     * Integration test verifying that deferral of state works
     * as expected -- namely, that highlighting is always captured,
     * but the state reflects whatever it was when record() was called.
     */
    public void testDeferral() {
        // Place a cell at origin
        put(origin, 1);

        // Record state
        query.record(cellLayer);

        // Perform a highlight at coordinate x
        query.highlight(x, 1);

        // Remove cell at origin
        cellLayer.getUpdateManager().banish(origin);

        // CellLayer should not report a cell at origin
        assertFalse(cellLayer.getViewer().isOccupied(origin));

        // StepState should report a cell at origin and highlight at coordinate x
        assertTrue(query.getRecordedCellLayer().getViewer().isOccupied(origin));

        Coordinate[] expectedHighlights = new Coordinate[] {x};
        Coordinate[] actualHighlights = query.getHighlights(1);
        assertArraysEqual(expectedHighlights, actualHighlights, false);
    }

    private void put(Coordinate c, int state) {
        MockCell cell = new MockCell(state);
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.place(cell, c);
    }

    public void testIsRecorded() {
        assertFalse(query.isRecorded());
        query.record(cellLayer);
        assertTrue(query.isRecorded());
    }
}
