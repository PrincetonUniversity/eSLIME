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

package layers;

import cells.BehaviorCell;
import cells.Cell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import io.deserialize.MockCoordinateDeindexer;
import layers.cell.CellLayer;
import layers.solute.LightweightSoluteLayer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dbborens on 3/26/14.
 *
 * TODO This class should be totally rewritten now that LSS has been refactored
 */
public class LightweightSystemStateTest extends SystemStateTest {

    private LightweightSystemState query;
    private Coordinate[] canonicals;
    private int[] stateVector = new int[]{1, 0, 2, 3, 2};
    private double[] healthVector = new double[]{1.0, 0.0, -0.1, 2.0, 1};
    private Geometry g;

    private String id = "id";
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        g = buildMockGeometry();
        canonicals = g.getCanonicalSites();
        deindexer.setUnderlying(canonicals);
        query = new LightweightSystemState(g);
        query.initCellLayer(stateVector, healthVector);

        query.initSoluteLayer(id, healthVector);
    }

    @Override
    public void testGetHealth() throws Exception {
        for (int i = 0; i < 4; i++) {
            Coordinate coord = canonicals[i];
            double expected = healthVector[i];
            Cell cell = query.getLayerManager().getCellLayer().getViewer().getCell(coord);
            double actual;
            if (cell == null) {
                actual = 0.0;
            } else {
                actual = cell.getHealth();
            }
            assertEquals(expected, actual, epsilon);
        }
    }

    @Override
    public void testGetState() throws Exception {
        for (int i = 0; i < 4; i++) {
            Coordinate coord = canonicals[i];
            int expected = stateVector[i];
            int actual = query.getLayerManager().getCellLayer().getViewer().getState(coord);
            assertEquals(expected, actual);
        }
    }

    @Override
    // This test has been made stupid by the refactor of LightweightSystemState and should be rethought.
    public void testGetValue() throws Exception {

        // Yuck.
        double[] data = query.getLayerManager().getSoluteLayer(id).getState().getSolution().getData();

        assertArraysEqual(healthVector, data, false);
    }

    @Override
    public void testGetTime() throws Exception {
        double expected = 0.7;
        query.setTime(expected);
        double actual = query.getTime();
        assertEquals(expected, actual, epsilon);
    }

    @Override
    public void testGetFrame() throws Exception {
        int expected = 7;
        query.setFrame(expected);
        int actual = query.getFrame();
        assertEquals(expected, actual);
    }

    @Override
    public void testIsHighlighted() throws Exception {
        int channelId = 0;
        Set<Coordinate> expected = new HashSet<>(1);
        expected.add(canonicals[2]);
        query.setHighlights(channelId, expected);

        assertTrue(query.isHighlighted(channelId, canonicals[2]));
        assertFalse(query.isHighlighted(channelId, canonicals[0]));
    }

    public void testGetLayerManager() throws Exception {
        MockLayerManager expected = new MockLayerManager();

        CellLayer cellLayer = new CellLayer(g);
        LightweightSoluteLayer soluteLayer = new LightweightSoluteLayer(g, expected, id);
        expected.setCellLayer(cellLayer);
        expected.addSoluteLayer(id, soluteLayer);

        for (int i = 0; i < g.getCanonicalSites().length; i++) {
            Coordinate c = g.getCanonicalSites()[i];
            int state = stateVector[i];
            double health = healthVector[i];

            if (state != 0) {
                BehaviorCell cell = new BehaviorCell(expected, state, health, 0.0);
                cellLayer.getUpdateManager().place(cell, c);
            }
            soluteLayer.set(c, health);
        }

        LayerManager actual = query.getLayerManager();
        assertEquals(expected, actual);
    }
}
