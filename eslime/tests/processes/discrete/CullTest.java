/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.MockCell;
import processes.BaseProcessArguments;
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
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, null);
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);
        query = new Cull(arguments, cpArguments, 0.5);
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

        live.setHealth(THRESHOLD + 0.1);
        die.setHealth(THRESHOLD - 0.1);

        // Place a cell above the threshold.
        cellLayer.getUpdateManager().place(live, x);

        // Place a cell below the threshold.
        cellLayer.getUpdateManager().place(die, y);

        // Verify that there are two live cells.
        assertEquals(2, cellLayer.getViewer().getOccupiedSites().size());

        // Cull.
        query.target();
        query.fire(new MockStepState());

        // Only the one above the threshold should survive.
        assertTrue(cellLayer.getViewer().isOccupied(x));
        assertEquals(1, cellLayer.getViewer().getOccupiedSites().size());
    }

    /**
     * Cull is supposed to kill any cells that are less than or equl to the
     * threshold. This test verifies that cells at the threshold are culled.
     *
     * @throws Exception
     */
    public void testBorderlineCase() throws Exception {
        MockCell borderline = new MockCell();
        borderline.setHealth(THRESHOLD);
        cellLayer.getUpdateManager().place(borderline, x);

        assertTrue(cellLayer.getViewer().isOccupied(x));

        // Cull.
        query.target();
        query.fire(new MockStepState());

        assertFalse(cellLayer.getViewer().isOccupied(x));
    }
}
