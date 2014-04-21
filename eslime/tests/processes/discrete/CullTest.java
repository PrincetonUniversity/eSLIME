/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package processes.discrete;

import cells.MockCell;
import processes.MockStepState;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 3/5/14.
 */
public class CullTest extends EslimeLatticeTestCase {

    private final double THRESHOLD = 0.5;

    private Cull query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new Cull(null, layerManager, 0, null, 0.5);
    }

    /**
     * Verify that cells clearly above and below the threshold are handled as
     * expected.
     *
     * @throws Exception
     */
    public void testLifeCycle() throws Exception {
        MockCell live = new MockCell();
        MockCell die = new MockCell();

        live.setFitness(THRESHOLD + 0.1);
        die.setFitness(THRESHOLD - 0.1);

        // Place a cell above the threshold.
        layer.getUpdateManager().place(live, x);

        // Place a cell below the threshold.
        layer.getUpdateManager().place(die, y);

        // Verify that there are two live cells.
        assertEquals(2, layer.getViewer().getOccupiedSites().size());

        // Cull.
        query.target();
        query.fire(new MockStepState());

        // Only the one above the threshold should survive.
        assertTrue(layer.getViewer().isOccupied(x));
        assertEquals(1, layer.getViewer().getOccupiedSites().size());
    }

    /**
     * Cull is supposed to kill any cells that are less than or equl to the
     * threshold. This test verifies that cells at the threshold are culled.
     *
     * @throws Exception
     */
    public void testBorderlineCase() throws Exception {
        MockCell borderline = new MockCell();
        borderline.setFitness(THRESHOLD);
        layer.getUpdateManager().place(borderline, x);

        assertTrue(layer.getViewer().isOccupied(x));

        // Cull.
        query.target();
        query.fire(new MockStepState());

        assertFalse(layer.getViewer().isOccupied(x));
    }
}
