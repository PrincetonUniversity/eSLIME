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

import java.util.Random;

/**
 * Functional test for the ExpandTo action, which utilizes a path-of-least-
 * resistance preferential division algorithm.
 */
public class ExpandToTest extends EslimeTestCase {

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

        // A cell exists in position 5 for all cases
        placeNumberedCell(5);
    }

    /**
     *   Parent and target sites have adjacent vacancy; divide toward vacancy.
     *   Population should grow in direction of vacancy.
     *
     *   0123456789
     *   ____45____  Initial condition
     *      <^       Cell 4 divides left
     *
     *   0123456789
     *   ___445____  Resulting condition
     *
     */
    public void testOutwardSymmetricDisplacement() throws Exception {
        Coordinate target = new Coordinate(3, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});
        parent.trigger("clone-self", null);

        checkPosition(3, 4);
        checkPosition(4, 4);
        checkPosition(5, 5);
    }

    /**
     *   Parent and target sites have adjacent vacancies; divide toward
     *   interior. Parent gets shoved; result should be shifted in direction
     *   of parent vacancy.
     *
     *   0123456789
     *   ____45____  Initial condition
     *       ^>      Cell 4 divides right. Parent and target are equidistant
     *               from vacancies, and the coin toss favors parent (4)
     *               getting shoved.
     *
     *   0123456789
     *   ___445____  Resulting condition
     *
     */
    public void testInwardSymmetricParentDisplacement() throws Exception {
        Coordinate target = new Coordinate(5, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});

        // The coin toss arbitrarily favors shoving parent on true.
        random.setBooleanValue(true);
        parent.trigger("clone-self", null);

        checkPosition(3, 4);
        checkPosition(4, 4);
        checkPosition(5, 5);
    }

    /**
     *   Parent and target sites have adjacent vacancies; divide toward
     *   interior. Target gets shoved; result should be shifted in direction
     *   of target vacancy.
     *
     *   0123456789
     *   ____45____  Initial condition
     *       ^>      Cell 4 divides right. Parent and target are equidistant
     *               from vacancies, and the coin toss favors target (5)
     *               getting shoved.
     *
     *   0123456789
     *   ____445___  Resulting condition
     */
    public void testInwardSymmetricTargetDisplacement() throws Exception {
        Coordinate target = new Coordinate(5, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});

        // The coin toss arbitrarily favors shoving parent on true.
        random.setBooleanValue(false);
        parent.trigger("clone-self", null);

        checkPosition(4, 4);
        checkPosition(5, 4);
        checkPosition(6, 5);
    }

    /**
     * Parent has an adjacent vacancy; target site does not. Divide toward
     * interior. Minimum distance from target site to vacancy is greater
     * than the minimum distance from parent site to vacancy. Therefore,
     * the parent will get shoved despite having divided toward the interior.
     *
     * 0123456789
     * ____456___  Initial condition
     *     ^>      Cell 4 divides right. Parent is closer to a vacancy than
     *             child. Parent gets shoved.
     *
     * 0123456789
     * ___4456___  Resulting condition
     *
     */
    public void testInwardAsymmetricDisplacement() throws Exception {
        // A cell exists in position 5 for all cases
        placeNumberedCell(6);

        Coordinate target = new Coordinate(5, 0, 0);
        parentTargetRule.setTargets(new Coordinate[] {target});

        // The coin toss arbitrarily favors shoving parent on true.
        random.setBooleanValue(true);
        parent.trigger("clone-self", null);

        checkPosition(3, 4);
        checkPosition(4, 4);
        checkPosition(5, 5);
        checkPosition(6, 6);
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

        ExpandTo expandTo = new ExpandTo(cell, layerManager, targetRule,
                null, null, random);

        Behavior behavior = new Behavior(cell, layerManager, new Action[] {expandTo});
        bd.map("clone-self", behavior);

        return targetRule;
    }

    private void checkPosition(int x, int state) {
        Coordinate c = new Coordinate(x, 0, 0);
        Cell cell = layer.getViewer().getCell(c);
        assertEquals(state, cell.getState());
    }
}