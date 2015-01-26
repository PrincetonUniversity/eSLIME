/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.action.ActionRangeMap;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/26/15.
 */
public class ActionRangeMapDescriptor {

    private Function<BehaviorCell, ActionRangeMap> constructor;

    public ActionRangeMapDescriptor(Function<BehaviorCell, ActionRangeMap> constructor) {
        this.constructor = constructor;
    }

    public ActionRangeMap instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }
}
