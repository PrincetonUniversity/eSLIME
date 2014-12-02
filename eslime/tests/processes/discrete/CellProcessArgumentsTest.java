/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import test.EslimeTestCase;

public class CellProcessArgumentsTest extends EslimeTestCase {

    private CoordinateSet activeSites;
    private Argument<Integer> maxTargets;
    private CellProcessArguments query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activeSites = new CustomSet();
        maxTargets = new ConstantInteger(0);

        query = new CellProcessArguments(activeSites, maxTargets);
    }

    public void testGetActiveSites() throws Exception {
        assert (query.getActiveSites() == activeSites);
    }

    public void testGetMaxTargets() throws Exception {
        assert (query.getMaxTargets() == maxTargets);
    }
}