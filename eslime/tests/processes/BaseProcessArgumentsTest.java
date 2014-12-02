/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes;

import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.ConstantInteger;
import test.EslimeLatticeTestCase;

public class BaseProcessArgumentsTest extends EslimeLatticeTestCase {

    private GeneralParameters p;
    private int id;
    private Argument<Integer> start;
    private Argument<Integer> period;

    private BaseProcessArguments query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
        id = 7;
        start = new ConstantInteger(4);
        period = new ConstantInteger(11);

        query = new BaseProcessArguments(layerManager, p, id, start, period);
    }

    public void testGetPeriod() throws Exception {
        assertTrue(period == query.getPeriod());
    }

    public void testGetStart() throws Exception {
        assertTrue(start == query.getStart());

    }

    public void testGetId() throws Exception {
        assertTrue(id == query.getId());

    }

    public void testGetGeneralParameters() throws Exception {
        assertTrue(p == query.getGeneralParameters());
    }

    public void testGetLayerManager() throws Exception {
        assertTrue(layerManager == query.getLayerManager());
    }
}