/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Function<BehaviorCell, RelationshipTuple> lookup;
    private ContinuumAgentNotifier notifier;
    public ContinuumAgentIndex(Function<BehaviorCell, RelationshipTuple> lookup) {
        this.lookup = lookup;
        notifier = buildNotifier();
        reset();
    }

    private ContinuumAgentNotifier buildNotifier() {
        Consumer<BehaviorCell> adder = cell -> add(cell);
        Consumer<BehaviorCell> remover = cell -> remove(cell);
        return new ContinuumAgentNotifier(adder, remover);
    }

    private void add(BehaviorCell cell) {
        index.put(cell, null);
    }

    /**
     * Retrieve relationships from indexed cells.
     *
     * @return
     */
    public HashSet<RelationshipTuple> getRelationShips() {
        HashSet<RelationshipTuple> ret = new HashSet<>(index.size());

        // What on earth did IntelliJ do to my humble iterator?
        ret.addAll(index.keySet().stream().map(lookup::apply).collect(Collectors.toList()));

        return ret;
    }

    private void remove(BehaviorCell cell) {
        if (!index.containsKey(cell)) {
            throw new IllegalStateException("Attempted to remove non-existent key from relationship index");
        }
        index.remove(cell);
    }

    public void reset() {
        index = new IdentityHashMap<>();
    }

    public ContinuumAgentNotifier getNotifier() {
        return notifier;
    }
}
