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

import agent.control.BehaviorDispatcher;
import agent.targets.MockTargetRule;
import cells.BehaviorCell;
import control.identifiers.Coordinate;
import test.EslimeLatticeTestCase;

import java.util.Random;

public class CloneToTest extends EslimeLatticeTestCase {

    private BehaviorCell original;
    private MockTargetRule targetRule;
    private CloneTo query;

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

        Random random = new Random(RANDOM_SEED);
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

    public void testReplacement() throws Exception {
        fail("Not yet implemented.");
    }

    public void testShoving() throws Exception {
        fail("Not yet implemented.");
    }
}