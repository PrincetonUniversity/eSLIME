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

import cells.MockCell;
import junit.framework.TestCase;

/**
 * Created by dbborens on 4/27/14.
 */
public class ActionRangeMapTest extends TestCase {
    public void testClone() throws Exception {
        MockCell originalCell = new MockCell(1);
        MockCell cloneCell = new MockCell(2);

        ActionRangeMap original = makeActionRangeMap(originalCell);
        ActionRangeMap expected = makeActionRangeMap(cloneCell);
        ActionRangeMap actual = original.clone(cloneCell);

        assertEquals(expected, actual);
        assertFalse(expected == actual);

        checkCallbacks(actual, cloneCell);
        checkCallbacks(original, originalCell);
    }

    private void checkCallbacks(ActionRangeMap map, MockCell cell) {
        for (Action key : map.getKeys()) {
            assertEquals(cell, key.getCallback());
        }
    }

    private ActionRangeMap makeActionRangeMap(MockCell cell) {
        ActionRangeMap ret = new ActionRangeMap(3);
        loadMockAction(ret, cell, 0.5, 1);
        loadMockAction(ret, cell, 0.75, 2);
        loadMockAction(ret, cell, 0.01, 3);
        return ret;
    }

    private void loadMockAction(ActionRangeMap map, MockCell cell, double weight, int identifier) {
        MockAction action = new MockAction();
        action.setIdentifier(identifier);
        action.setCallback(cell);
        map.add(action, weight);
    }
}
