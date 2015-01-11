/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;

import java.util.IdentityHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Agents can directly manipulate the continuum value at the coordinate where
 * they are located. This class tracks the set of all agents capable of
 * manipulating the state of this continuum in a particular way, such as by
 * exponentiating the local concentration, or directly adding to it.
 *
 */
public class ContinuumAgentIndex {

    private IdentityHashMap<BehaviorCell, Supplier<RelationshipTuple>> map;
    private Function<BehaviorCell, RelationshipTuple> lookup;

    public ContinuumAgentIndex(Function<BehaviorCell, RelationshipTuple> lookup) {
        this.lookup = lookup;
        reset();
    }

    public void reset() {
        map = new IdentityHashMap<>();
    }

    private void add(BehaviorCell cell) {
        if (map.containsKey(cell)) {
            throw new IllegalStateException("Attempted to add existing cell to relationship index");
        }
        Supplier<RelationshipTuple> supplier = () -> lookup.apply(cell);
        map.put(cell, supplier);
    }

    private void remove(BehaviorCell cell) {
        if (!map.containsKey(cell)) {
            throw new IllegalStateException("Attempted to remove non-existent key from relationship index");
        }
        map.remove(cell);
    }

    public Stream<RelationshipTuple> getRelationships() {
        return map.values()
                .stream()
                .map(Supplier::get);
    }

    public ContinuumAgentNotifier getNotifier() {
        Consumer<BehaviorCell> adder = cell -> add(cell);
        Consumer<BehaviorCell> remover = cell -> remove(cell);
        return new ContinuumAgentNotifier(adder, remover);
    }

}
