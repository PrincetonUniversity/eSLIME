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

public class ExpandTest extends EslimeTestCase {

    private MockLayerManager layerManager;
    private BehaviorCell parent;
    private MockRandom random;
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
        random = new MockRandom();

        // Place the parent at site 4 and get its target rule
        parentTargetRule = placeNumberedCell(4);
        parent = (BehaviorCell) layer.getViewer().getCell(new Coordinate(4, 0, 0));

    }

    /**
     *   Nearest vacancy is left; cell divides left.
     *
     *   0123456789
     *   ____45____  Initial condition
     *       ^       Cell 4 divides left
     *
     *   0123456789
     *   ___445____  Resulting condition
     *
     */
    public void testOneVacancy() throws Exception {
        // Place blocking cell
        placeNumberedCell(5);

        Coordinate target = new Coordinate(3, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("clone-self", null);

        checkPosition(3, 4);
        checkPosition(4, 4);
        checkPosition(5, 5);
    }

    /**
     * Both sides vacant; outcome depends on random number value.
     *
     *   0123456789
     *   ____4_____  Initial condition
     *
     *   0123456789
     *   ___44_____  Case 1
     *
     *   0123456789
     *   ____44____  Case 2
     *
     *   Since the difference between case 1 and case 2 is handled
     *   in a helper object (ShoveHelper) which is tested separately,
     *   we consider only case 1.
     */
    public void testMultipleVacancies() throws Exception {

        Coordinate target = new Coordinate(3, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("clone-self", null);

        checkPosition(3, 4);
        checkPosition(4, 4);
    }

    /**
     * Both sides occupied, one has closer vacancy
     *
     *   0123456789
     *   __2345____  Initial condition
     *       ^
     *   0123456789
     *   __23445___  Resulting condition
     *
     */
    public void testUnequalBarrierWidths() throws Exception {
        // Place blocking cell
        placeNumberedCell(2);
        placeNumberedCell(3);
        placeNumberedCell(5);

        Coordinate target = new Coordinate(3, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("clone-self", null);

        checkPosition(2, 2);
        checkPosition(3, 3);
        checkPosition(4, 4);
        checkPosition(5, 4);
        checkPosition(6, 5);
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

        Expand expand = new Expand(cell, layerManager, null, null, random);

        Behavior behavior = new Behavior(cell, layerManager, new Action[] {expand});
        bd.map("clone-self", behavior);

        return targetRule;
    }

    private void checkPosition(int x, int state) {
        Coordinate c = new Coordinate(x, 0, 0);
        Cell cell = layer.getViewer().getCell(c);
        assertEquals(state, cell.getState());
    }
}