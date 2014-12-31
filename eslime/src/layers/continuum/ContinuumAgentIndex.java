/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import control.identifiers.Coordinate;

import java.util.HashMap;
import java.util.IdentityHashMap;

/**
 * Agents can directly manipulate the continuum value at the coordinate where
 * they are located. This class tracks the set of all agents capable of
 * manipulating the state of this continuum in a particular way, such as by
 * exponentiating the local concentration, or directly adding to it.
 *
 */
public class ContinuumAgentIndex {

    // Very many cells can be logically equivalent because they are
    // identical agents; we want to track the relationships by name.
    private IdentityHashMap<BehaviorCell, Object> index;

    private AgentRelationshipLookup lookup;

    public ContinuumAgentIndex(AgentRelationshipLookup lookup) {
        this.lookup = lookup;
        reset();
    }

    public void add(BehaviorCell cell) {
        index.put(cell, null);
    }

    /**
     * Retrieve relationships from indexed cells.
     *
     * @return
     */
    public HashMap<Coordinate, Double> getRelationShips() {
        HashMap<Coordinate, Double> ret = new HashMap<>(index.size());

        // I bet there's a nice way to do this with lambdas
        for (BehaviorCell cell : index.keySet()) {
            RelationshipTuple tuple = lookup.go(cell);
            ret.put(tuple.getCoordinate(), tuple.getMagnitude());
        }
        return ret;
    }

    public void remove(BehaviorCell cell) {
        if (!index.containsKey(cell)) {
            throw new IllegalStateException("Attempted to remove non-existent key from relationship index");
        }
        index.remove(cell);
    }

    public void reset() {
        index = new IdentityHashMap<>();
    }
}
