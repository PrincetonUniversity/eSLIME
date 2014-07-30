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

package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import agent.targets.MockTargetRule;
import cells.BehaviorCell;
import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.MockGeometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.Periodic;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import test.EslimeLatticeTestCase;

import java.util.Random;

public class CloneToTest extends EslimeLatticeTestCase {

    private BehaviorCell original;
    private MockTargetRule targetRule;
    private CloneTo query;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Create mock targeter that lists other two sites as targets.
        targetRule = new MockTargetRule();
        Coordinate[] targets = new Coordinate[] {x, y};
        targetRule.setTargets(targets);

        // Place a single cell at origin.
        original = new BehaviorCell(layerManager, 1, 1.0, 1.0);
        BehaviorDispatcher bd = new BehaviorDispatcher();
        original.setDispatcher(bd);

        cellLayer.getUpdateManager().place(original, origin);

        random = new Random(RANDOM_SEED);
        // Create query.
        query = new CloneTo(original, layerManager, targetRule, false, null,
                null, random);
    }

    public void testLifeCycle() throws Exception {
        // Trigger the clone event.
        query.run(null);

        // The other two sites should be occupied.
        assertTrue(cellLayer.getViewer().isOccupied(x));
        assertTrue(cellLayer.getViewer().isOccupied(y));

        // The cells at the other sites should each be equal to the original.
        assertEquals(original, cellLayer.getViewer().getCell(x));
        assertEquals(original, cellLayer.getViewer().getCell(y));
        assertFalse(original == cellLayer.getViewer().getCell(y));
    }

    /**
     * Integration test using replacement process.
     *
     * @throws Exception
     */
    public void testReplacement() throws Exception {
        CellLayer layer = linearLayer(false);
        Cell cell = layer.getViewer().getCell(new Coordinate(4, 0, 0));

        // Divide cell at position 4 toward 5
        cell.trigger("clone-self", null);

        // New configuration: _123446_89
        assertEquals(4, layer.getViewer().getState(new Coordinate(4, 0, 0)));
        assertEquals(4, layer.getViewer().getState(new Coordinate(5, 0, 0)));
        assertEquals(6, layer.getViewer().getState(new Coordinate(6, 0, 0)));
        assertFalse(layer.getViewer().isOccupied(new Coordinate(7, 0, 0)));
    }

    /**
     *
     *   _123456_89  Initial condition
     *       ^       (Cell to be divided)
     */
    private CellLayer linearLayer(boolean shoving) {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 10, 1);
        Boundary boundary = new Periodic(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CellLayer layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        placeCells(layer, shoving);

        return layer;
    }
    private void placeNumberedCell(int x, CellLayer layer, boolean shoving) {
        BehaviorCell cell = new BehaviorCell(layerManager, x, x, x);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
        BehaviorDispatcher bd = new BehaviorDispatcher();
        cell.setDispatcher(bd);

        MockTargetRule mtr = new MockTargetRule();

        // Cells always divide to the right
        mtr.setTargets(new Coordinate[] {new Coordinate(x + 1, 0, 0)});

        CloneTo cloneTo = new CloneTo(cell, layerManager, mtr,
                shoving, null, null, random);

        Behavior behavior = new Behavior(cell, layerManager, new Action[] {cloneTo});
        bd.map("clone-self", behavior);

    }

    private void placeCells(CellLayer layer, boolean shoving) {
        for (int x = 1; x < 7; x++) {
            placeNumberedCell(x, layer, shoving);
        }

        for (int x = 8; x <= 9; x++) {
            placeNumberedCell(x, layer, shoving);
        }
    }

}