/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import control.GeneralParameters;
import test.EslimeLatticeTestCase;

public class CellDescriptorTest extends EslimeLatticeTestCase {
    private static final int CELL_STATE = 1;
    private static final double THRESHOLD = 0.5;
    private static final double INIT_HEALTH = 0.75;
    private CellDescriptor query;
    private GeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        p = makeMockGeneralParameters();
        query = new CellDescriptor(layerManager, p);
        query.setCellState(new ConstantInteger(CELL_STATE));
        query.setInitialHealth(new ConstantDouble(INIT_HEALTH));
        query.setThreshold(new ConstantDouble(THRESHOLD));
    }

    public void testNext() throws Exception {
        BehaviorCell expected = new BehaviorCell(layerManager, CELL_STATE, INIT_HEALTH, THRESHOLD);
        BehaviorDispatcher dispatcher = new BehaviorDispatcher(null, expected, layerManager, p);
        expected.setDispatcher(dispatcher);
        Cell actual = query.next();

        assertEquals(expected, actual);
        assertFalse(expected == actual);
    }

    public void testEquals() throws Exception {
        CellDescriptor other = new CellDescriptor(layerManager, p);
        other.setCellState(new ConstantInteger(CELL_STATE));
        other.setInitialHealth(new ConstantDouble(INIT_HEALTH));
        other.setThreshold(new ConstantDouble(THRESHOLD));

        assertEquals(query, other);
        assertFalse(query == other);
    }
}