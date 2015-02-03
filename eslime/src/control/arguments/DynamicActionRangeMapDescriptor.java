/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.Behavior;
import agent.action.DynamicActionRangeMap;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/26/15.
 */
@Deprecated
public class DynamicActionRangeMapDescriptor {
    
    private Function<BehaviorCell, DynamicActionRangeMap> constructor;
    
    public DynamicActionRangeMapDescriptor(Function<BehaviorCell, DynamicActionRangeMap> constructor) {
        this.constructor = constructor;    
    }
    
    public DynamicActionRangeMap instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }
}
