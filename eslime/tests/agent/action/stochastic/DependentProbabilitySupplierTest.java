/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;
import test.TestBase;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependentProbabilitySupplierTest extends TestBase {

    private Function<BehaviorCell, Double> valueLookup;
    private double coefficient;
    private double offset;
    private BehaviorCell cell, child;

    private DependentProbabilitySupplier query;

    @Before
    public void init() throws Exception {
        cell = mock(BehaviorCell.class);
        child = mock(BehaviorCell.class);

        valueLookup = (Function<BehaviorCell, Double>) mock(Function.class);
        when(valueLookup.apply(cell)).thenReturn(1.0);
        when(valueLookup.apply(child)).thenReturn(2.0);

        coefficient = 3.0;
        offset = 0.5;

        query = new DependentProbabilitySupplier(valueLookup, cell, coefficient, offset);
    }

    @Test
    public void getAppliesAllArguments() throws Exception {
        assertEquals(3.5, query.get(), epsilon);
    }

    @Test
    public void cloneReturnsEquivalent() throws Exception {
        DependentProbabilitySupplier clone = query.clone(child);
        assertEquals(6.5, clone.get(), epsilon);
    }

}