/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import control.identifiers.Coordinate;

/**
 * Created by dbborens on 3/6/14.
 */
public class NullAction extends Action {
    public NullAction() {
        super(null, null);
    }

    @Override
    public void run(Coordinate caller) {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NullAction) {
            return true;
        }

        return false;
    }

    @Override
    public Action clone(BehaviorCell child) {
        return new NullAction();
    }
}
