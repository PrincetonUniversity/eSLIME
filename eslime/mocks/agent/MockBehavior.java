/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent;

import agent.action.Action;
import cells.BehaviorCell;
import control.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class MockBehavior extends Behavior {

    private int timesRun;
    private HashMap<Coordinate, Integer> callerCounts;

    private Coordinate lastCaller = null;


    public MockBehavior() {
        super(null, null, null);
        timesRun = 0;
        callerCounts = new HashMap<>();
    }

    public MockBehavior(BehaviorCell callback) {
        super(callback, null, null);
        timesRun = 0;
        callerCounts = new HashMap<>();
    }

    public Coordinate getLastCaller() {
        return lastCaller;
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

    public int timesCaller(Coordinate caller) {
        if (!callerCounts.containsKey(caller))
            return 0;

        return callerCounts.get(caller);
    }

    @Override
    protected Action[] getActionSequence() {
        return new Action[0];
    }

    @Override
    public Behavior clone(BehaviorCell child) {
        return new MockBehavior(child);
    }

}
