/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
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
        throw new UnsupportedOperationException("Clone using the clone(BehaviorCell child) method.");
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
