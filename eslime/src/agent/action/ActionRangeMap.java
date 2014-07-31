/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import structural.RangeMap;

/**
 * Created by dbborens on 4/27/14.
 */
public class ActionRangeMap extends RangeMap<Action> {
    public ActionRangeMap(int initialSize) {
        super(initialSize);
    }

    public ActionRangeMap() {
        super();
    }

    @Override
    public RangeMap<Action> clone() {
        throw new UnsupportedOperationException("Clone using the replicate(BehaviorCell child) method.");
    }

    public ActionRangeMap clone(BehaviorCell child) {
        int n = keys.size();
        ActionRangeMap cloned = new ActionRangeMap(n);

        for (int i = 1; i < floors.size(); i++) {
            Action key = keys.get(i - 1);
            Action clonedKey = key.clone(child);
            Double weight = floors.get(i) - floors.get(i - 1);

            cloned.add(clonedKey, weight);
        }

        return cloned;
    }
}
