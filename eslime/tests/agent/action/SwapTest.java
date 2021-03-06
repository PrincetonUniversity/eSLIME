/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
import test.EslimeTestCase;

import java.util.ArrayList;
import java.util.List;

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
        List<Coordinate> targets = new ArrayList<>(1);
        targets.add(target);
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
        List<Coordinate> targets = new ArrayList<>(1);
        targets.add(target);
        parentTargetRule.setTargets(targets);
        parent.trigger("swap", null);

        checkIsVacant(3);
        checkIsVacant(4);
        checkPosition(5, 4);
    }


    private MockTargetRule placeNumberedCell(int x) throws Exception {
        BehaviorCell cell = new BehaviorCell(layerManager, x, x, x);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
        BehaviorDispatcher bd = new BehaviorDispatcher();
        cell.setDispatcher(bd);

        MockTargetRule targetRule = new MockTargetRule();

        // Cells always divide to the right
        List<Coordinate> targets = new ArrayList<>(1);
        Coordinate target = new Coordinate(x + 1, 0, 0);
        targets.add(target);
        targetRule.setTargets(targets);

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