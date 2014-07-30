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

package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import agent.targets.MockTargetRule;
import cells.BehaviorCell;
import cells.Cell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.Periodic;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.MockRandom;
import test.EslimeTestCase;

public class SwapTest extends EslimeTestCase {

    private MockLayerManager layerManager;
    private BehaviorCell parent;
    private CellLayer layer;
    private MockTargetRule parentTargetRule;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Lattice lattice = new RectangularLattice();
        layerManager = new MockLayerManager();
        Shape shape = new Rectangle(lattice, 10, 1);
        Boundary boundary = new Periodic(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);

        // Place the parent at site 4 and get its target rule
        parentTargetRule = placeNumberedCell(4);
        parent = (BehaviorCell) layer.getViewer().getCell(new Coordinate(4, 0, 0));

    }

    /**
     *
     *   0123456789
     *   ____45____  Initial condition
     *       ^^
     *   0123456789
     *   ____54____  Resulting condition
     *
     */
    public void testTwoOccupied() throws Exception {
        placeNumberedCell(5);
        Coordinate target = new Coordinate(5, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("swap", null);

        checkIsVacant(3);
        checkPosition(4, 5);
        checkPosition(5, 4);
        checkIsVacant(6);
    }

    /**
     *
     *   0123456789
     *   ____4_____  Initial condition
     *       ^^
     *   0123456789
     *   _____4____  Resulting condition
     *
     */
    public void testOneVacant() throws Exception {
        Coordinate target = new Coordinate(5, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("swap", null);

        checkIsVacant(3);
        checkIsVacant(4);
        checkPosition(5, 4);
    }


    private MockTargetRule placeNumberedCell(int x) {
        BehaviorCell cell = new BehaviorCell(layerManager, x, x, x);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
        BehaviorDispatcher bd = new BehaviorDispatcher();
        cell.setDispatcher(bd);

        MockTargetRule targetRule = new MockTargetRule();

        // Cells always divide to the right
        targetRule.setTargets(new Coordinate[]{new Coordinate(x + 1, 0, 0)});

        Action action = new Swap(cell, layerManager, targetRule, null, null);

        Behavior behavior = new Behavior(cell, layerManager, new Action[] {action});
        bd.map("swap", behavior);

        return targetRule;
    }

    private void checkPosition(int x, int state) {
        Coordinate c = new Coordinate(x, 0, 0);
        Cell cell = layer.getViewer().getCell(c);
        assertEquals(state, cell.getState());
    }

    private void checkIsVacant(int x) {
        Coordinate c = new Coordinate(x, 0, 0);
        assertFalse(layer.getViewer().isOccupied(c));
    }

}