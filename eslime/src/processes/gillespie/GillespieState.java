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

package processes.gillespie;

import java.util.HashMap;

/**
 * @author David Bruce Borenstein
 * @tested GillespieTest.java
 */
public class GillespieState {

    // Map of process ID --> weight
    private HashMap<Integer, Double> weightMap;

    // Map of process ID --> count
    private HashMap<Integer, Integer> countMap;

    private Integer[] expectedKeys;

    boolean closed = false;

    public GillespieState(Integer[] expectedKeys) {
        this.expectedKeys = expectedKeys;

        weightMap = new HashMap<>(expectedKeys.length);
        countMap = new HashMap<>(expectedKeys.length);
    }

    public void add(Integer processId, Integer eventCount, Double weight) {
        if (closed) {
            throw new IllegalStateException("Attempted to modify Gillespie state after it was closed.");
        }

        if (weightMap.containsKey(processId) || countMap.containsKey(processId)) {
            throw new IllegalStateException("Repeat assignment to Gillespie state.");
        }

        weightMap.put(processId, weight);
        countMap.put(processId, eventCount);
    }

    /**
     * Closes the state object to further additions, paving the way for
     * choosing an event to fire. This must be called before using getters
     * (except getKeys).
     */
    public void close() {
        if (closed) {
            throw new IllegalStateException("Repeat call to close() in Gillespie process state.");
        }

        if (weightMap.keySet().size() != expectedKeys.length) {
            throw new IllegalStateException("Gillespie state consistency failure.");
        }

        closed = true;
    }

    public Integer[] getKeys() {
        return expectedKeys.clone();
    }

    /**
     * Returns the total weight of all processes.
     *
     * @return
     */
    public double getTotalWeight() {
        if (!closed) {
            throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
        }

        double total = 0;
        for (Integer key : expectedKeys) {
            total += weightMap.get(key);
        }

        return total;
    }

    public double getWeight(Integer processId) {
        if (!closed) {
            throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
        }

        return weightMap.get(processId);
    }

    /**
     * Returns number of unique events possible for a given
     * process.
     *
     * @param processID
     */
    public int getEventCount(Integer processID) {
        if (!closed) {
            throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
        }

        return countMap.get(processID);

    }

    public boolean isClosed() {
        return closed;
    }

    public int getTotalCount() {
        if (!closed) {
            throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
        }

        int totalCount = 0;

        for (Integer key : countMap.keySet()) {
            totalCount += countMap.get(key);
        }

        return totalCount;
    }


}
