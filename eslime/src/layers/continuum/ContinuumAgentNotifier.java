/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Notifies a ContinuumAgentIndex that a related cell has been added or
 * removed.
 *
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentNotifier {

    private BiConsumer<BehaviorCell, Supplier<RelationshipTuple>> adder;
    private Consumer<BehaviorCell> remover;

    public ContinuumAgentNotifier(BiConsumer<BehaviorCell, Supplier<RelationshipTuple>> adder, Consumer<BehaviorCell> remover) {
        this.adder = adder;
        this.remover = remover;
    }

    public void add(BehaviorCell cell, Supplier<RelationshipTuple> supplier) {
        adder.accept(cell, supplier);
    }

    public void remove(BehaviorCell cell) {
        remover.accept(cell);
    }
}
