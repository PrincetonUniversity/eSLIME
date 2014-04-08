/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
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
    private HashMap<Coordinate, Integer> callerCounts;

    public Coordinate getLastCaller() {
        return lastCaller;
    }

    private Coordinate lastCaller;

    @Override
    public Action clone(BehaviorCell child) {
        return new MockAction();
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockAction);
    }

//    public int timesCaller(Coordinate caller) {
//        if (!callerCounts.containsKey(caller))
//            return 0;
//
//        return callerCounts.get(caller);
//    }

}
