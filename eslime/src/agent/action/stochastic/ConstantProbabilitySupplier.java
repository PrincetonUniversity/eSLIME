/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import cells.BehaviorCell;

/**
 * Created by dbborens on 1/9/15.
 */
public class ConstantProbabilitySupplier extends ProbabilitySupplier {
    private final Double value;

    public ConstantProbabilitySupplier(Double value) {
        this.value = value;
    }

    @Override
    public ProbabilitySupplier clone(BehaviorCell child) {
        return new ConstantProbabilitySupplier(value);
    }

    @Override
    public Double get() {
        return value;
    }
}
