/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package agent;

import agent.action.Action;
import cells.BehaviorCell;
import structural.identifiers.Coordinate;

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
