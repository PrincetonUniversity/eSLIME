/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.action.Action;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Descriptive, lightweight wrapper for a function that returns
 * a particular kind of action, given a behavior cell to use it.
 *
 * Created by dbborens on 1/24/15.
 */
public class ActionDescriptor<T extends Action> {

    private Function<BehaviorCell, T> constructor;

    public ActionDescriptor(Function<BehaviorCell, T> constructor) {
        this.constructor = constructor;
    }

    public T instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }
}
