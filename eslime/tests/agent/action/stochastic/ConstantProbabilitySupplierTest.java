/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import org.junit.Before;
import org.junit.Test;
import test.TestBase;

import static org.junit.Assert.assertEquals;

public class ConstantProbabilitySupplierTest extends TestBase {

    ConstantProbabilitySupplier query;

    @Before
    public void init() throws Exception {
        query = new ConstantProbabilitySupplier(1.0);
    }

    @Test
    public void getReturnsOriginalValue() throws Exception {
        assertEquals(1.0, query.get(), epsilon);
    }

    @Test
    public void cloneReturnsOriginalValue() throws Exception {
        ConstantProbabilitySupplier clone = query.clone(null);
        assertEquals(1.0, clone.get(), epsilon);
    }
}