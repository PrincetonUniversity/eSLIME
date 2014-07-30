/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import control.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 1/22/14.
 */
public class MockAction extends Action {
    private int timesRun;
    private BehaviorCell callback = null;

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    private int identifier = 0;
    private HashMap<Coordinate, Integer> callerCounts;

    public Coordinate getLastCaller() {
        return lastCaller;
    }

    private Coordinate lastCaller;

    @Override
    public Action clone(BehaviorCell child) {
        MockAction clone = new MockAction();
        clone.setCallback(child);
        clone.setIdentifier(identifier);
        return clone;
    }

    public MockAction() {
        super(null, null);
        timesRun = 0;
        callerCounts = new HashMap<>();
    }

    @Override
    public void run(Coordinate caller) {
        timesRun++;
        lastCaller = caller;
        if (caller != null) {
            increment(caller);
        }
    }

    private void increment(Coordinate caller) {
        if (!callerCounts.containsKey(caller)) {
            callerCounts.put(caller, 0);
        }

        int count = callerCounts.get(caller);
        callerCounts.put(caller, count + 1);
    }

    public int getTimesRun() {
        return timesRun;
    }

    public void setCallback(BehaviorCell callback) {
        this.callback = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockAction that = (MockAction) o;

        if (identifier != that.identifier) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return callback != null ? callback.hashCode() : 0;
    }

    public BehaviorCell getCallback() {
        return callback;
    }
}
