/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.Behavior;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/24/15.
 */
public class BehaviorDescriptor extends ActionDescriptor<Behavior> {

    public BehaviorDescriptor(Function<BehaviorCell, Behavior> constructor) {
        super(constructor);
    }

    public Behavior instantiate(BehaviorCell cell) {
        return super.instantiate(cell);
    }
}
