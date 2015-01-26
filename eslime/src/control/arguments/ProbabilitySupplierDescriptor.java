/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.Behavior;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/26/15.
 */
public class ProbabilitySupplierDescriptor<T extends ProbabilitySupplier> {

    private Function<BehaviorCell, T> constructor;

    public ProbabilitySupplierDescriptor(Function<BehaviorCell, T> constructor) {
        this.constructor = constructor;
    }

    public T instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }
}
