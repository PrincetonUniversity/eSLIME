/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import cells.BehaviorCell;

import java.util.function.Supplier;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class ProbabilitySupplier implements Supplier<Double> {

    public abstract ProbabilitySupplier clone(BehaviorCell child);
}
