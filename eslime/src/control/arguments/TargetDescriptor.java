/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;
import agent.targets.TargetRule;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/26/15.
 */
public class TargetDescriptor<T extends TargetRule> {

    private Function<BehaviorCell, T> constructor;

    public TargetDescriptor (Function<BehaviorCell, T> constructor) {
        this.constructor = constructor;
    }

    public T instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }

}
